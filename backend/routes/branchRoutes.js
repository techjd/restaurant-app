const express = require("express");
const Branches = require("../models/branches");
const Orders = require("../models/orders");
const router = express.Router();
const { check, validationResult } = require("express-validator");
const jwt = require("jsonwebtoken");
const bcrypt = require("bcryptjs");
const branch = require("../middleware/branch")
// Get All Food Items
router.get("/", async (req, res) => {
  const branches = await Branches.find({});
  res.json(branches);
});

// Get A Branch By ID
router.get("/:id", async (req, res) => {
  const branch = await Branches.findById(req.params.id);
  if (branch) {
    res.json(branch);
  } else {
    res.status(404).json({ msg: "Not Found" });
  }
});

//Get All Orders of Branch
router.get("/get/AllOrders", branch ,async (req, res) => {

  const branch = await Branches.findById(req.branch.id).select("-password");
  const branchId = branch.id


  const orders = await Orders.find({ branch: branchId }).sort('-createdAt');


  res.json(orders);
});

// Create a new branch

router.post("/addBranch",  [
  check("branchEmail", "Branch Email is required").isEmail(),
  check("branchAddress", "Branch Address is requires").not().isEmpty(),
  check("branchName", "Branch Name is required").not().isEmpty(),
  check(
    "branchPassword",
    "Please Include a Password for more than six characters for your branch"
  ).isLength({ min: 6 }),
  check("branchNumber", "Branch Phone Number is required").isNumeric(),
], async (req, res) => {
  const errors = validationResult(req);

  if (!errors.isEmpty()) {
    return res.status(400).json(errors.array());
  }

  const { branchEmail, branchName, branchAddress ,branchPassword, branchNumber } = req.body;

  try {
    let branch = await Branches.findOne({ branchEmail });

    if (branch) {
      return res.status(400).json({ msg: "Branch Already Existed" });
    }

    branch = new Branches({
      branchEmail,
      branchAddress,
      branchName,
      branchPassword,
      branchNumber,
    });

    const salt = await bcrypt.genSalt(10);

    branch.branchPassword = await bcrypt.hash(branchPassword, salt);

    console.log(branch);

    await branch.save();

    const payload = {
      branch: {
        id: branch.id,
      },
    };

    console.log("Getting JWT for Branch");

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
})

// Login for a new Branch

router.post("/loginBranch", [
  check("branchEmail", "Please include a valid email").isEmail(),
  check("branchPassword", "Password is Requires").exists(),
], async (req, res) => {
  const errors = validationResult(req);

  if (!errors.isEmpty()) {
    return res.status(400).json(errors.array());
  }

  const { branchEmail, branchPassword } = req.body;

  try {
    let branch = await Branches.findOne({ branchEmail });

    if (!branch) {
      return res.status(400).json({ msg: "Invalid Credentials" });
    }

    const isMatch = await bcrypt.compare(branchPassword, branch.branchPassword);

    if (!isMatch) {
      return res.status(400).json({ msg: "Invalid Credentials" });
    }

    const payload = {
      branch: {
        id: branch.id,
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
})

// Get the current logged in branch email
router.get("/get/current/branch", branch, async (req, res) => {
  try {
    const branch = await Branches.findById(req.branch.id).select("-password");
    console.log(branch.id);
    res.json(branch);
  } catch (err) {
    console.error(err);
    res.status(500).json({ msg: "Server Error" });
  }
});
module.exports = router;
