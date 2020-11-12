const mongoose = require("mongoose");
const branchesSchemas = mongoose.Schema(
  {
    branchEmail: {
      type: String,
      required: true,
    },

    branchName: {
      type: String,
      required: true,
    },
    branchPassword: {
      type: String,
      required: true,
    },
    branchAddress: {
      type: String,
      required: true,
    },
    branchNumber: {
      type: Number,
      required: true
    }
  },
  {
    timestamps: true,
  }
);

const Branches = mongoose.model("Branches", branchesSchemas);

module.exports = Branches;
