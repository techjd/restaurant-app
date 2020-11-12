const jwt = require("jsonwebtoken");
const dotenv = require("dotenv");

module.exports = function (req, res, next) {
  // Get Token from header
  const token = req.header("token");

  // Check if No Token

  if (!token) {
    return res.status(401).json({ msg: "No Token" });
  }

  // Verify Token

  try {
    const decoded = jwt.verify(token, process.env.JWTSECRET);

    req.branch = decoded.branch;

    next();
  } catch (err) {
    console.error(err);
    res.status(401).json({ msg: "Token is not valid" });
  }
};
