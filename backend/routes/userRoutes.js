const express = require("express");
const Users = require("../models/users");
const dotenv = require("dotenv");
const { check, validationResult } = require("express-validator");
const jwt = require("jsonwebtoken");
const bcrypt = require("bcryptjs");
const { OAuth2Client } = require("google-auth-library");
const auth = require("../middleware/auth");

const router = express.Router();
const client = new OAuth2Client(process.env.CLIENT_ID);

router.post("/addUser", async (req, res) => {
  const token = req.headers.token;

  try {
    const ticket = await client.verifyIdToken({
      idToken: token,
      audience: process.env.CLIENT_ID,
    });

    const payload = ticket.getPayload();

    const { sub, email, name } = payload;
    const newUser = new Users({
      sub,
      email,
      name,
    });

    let user = await Users.findOne({ email });

    if (!user) {
      await newUser.save();
    } else {
    }
  } catch (error) {
    console.error(error);
  }
});

//
router.get("/", auth, async (req, res) => {
  try {
    const user = await Users.findById(req.user.id).select("-password");
    console.log(user.id);
    res.json(user);
  } catch (err) {
    console.error(err);
    res.status(500).json({ msg: "Server Error" });
  }
});
// Add A User
router.post(
  "/registerUser",
  [
    check("email", "Email is required").isEmail(),
    check("name", "Name is required").not().isEmpty(),
    check(
      "password",
      "Please Include a Password for more than six characters"
    ).isLength({ min: 6 }),
    check("number", "Phone Number is required").isNumeric(),
  ],
  async (req, res) => {
    const errors = validationResult(req);

    if (!errors.isEmpty()) {
      return res.status(400).json(errors.array());
    }

    const { email, name, password, number } = req.body;

    try {
      let user = await Users.findOne({ email });

      if (user) {
        return res.status(400).json({ msg: "User Already Existed" });
      }

      user = new Users({
        email,
        name,
        password,
        number,
      });

      const salt = await bcrypt.genSalt(10);

      user.password = await bcrypt.hash(password, salt);

      console.log(user);

      await user.save();

      const payload = {
        user: {
          id: user.id,
        },
      };

      console.log("Getting JWT");

      jwt.sign(
        payload,
        process.env.JWTSECRET,
        { expiresIn: 36000000 },
        (err, token) => {
          if (err) throw err;
          res.json({ token });
        }
      );
    } catch (error) {
      console.error(error);
      res.status(500).json({ msg: "Server Error" });
    }
  }
);

router.post(
  "/loginUser",
  [
    check("email", "Please include a valid email").isEmail(),
    check("password", "Password is Requires").exists(),
  ],
  async (req, res) => {
    const errors = validationResult(req);

    if (!errors.isEmpty()) {
      return res.status(400).json(errors.array());
    }

    const { email, password } = req.body;

    try {
      let user = await Users.findOne({ email });

      if (!user) {
        return res.status(400).json({ msg: "Invalid Credentials" });
      }

      const isMatch = await bcrypt.compare(password, user.password);

      if (!isMatch) {
        return res.status(400).json({ msg: "Invalid Credentials" });
      }

      const payload = {
        user: {
          id: user.id,
        },
      };
      console.log("getting JWT");
      jwt.sign(
        payload,
        process.env.JWTSECRET,
        { expiresIn: 36000000 },
        (err, token) => {
          if (err) throw err;
          res.json({ token });
        }
      );
    } catch (error) {
      console.error(error);
      res.status(500).json({ msg: "Server Error" });
    }
  }
);
module.exports = router;
