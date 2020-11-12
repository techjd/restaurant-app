import fs from "fs";
import mongoose from "mongoose";
import colors from "colors";
import dotenv from "dotenv";
import connectDB from "./config/db.js";
import FoodItems from "./models/items.js";
import Branches from "./models/branches.js";
import data from "./data/data.js";
import branches from "./data/branches.js";
// Load env vars
dotenv.config();

// Connect DB
connectDB();

// Import into DB
const importData = async () => {
  try {
    await FoodItems.insertMany(data);
    await Branches.insertMany(branches);

    console.log("Data Imported...".green.inverse);
    process.exit();
  } catch (err) {
    console.error(err);
  }
};

// Delete data
const deleteData = async () => {
  try {
    await FoodItems.deleteMany();
    await Branches.deleteMany();
    console.log("Data Destroyed...".red.inverse);
    process.exit();
  } catch (err) {
    console.error(err);
  }
};

if (process.argv[2] === "-d") {
  deleteData();
} else {
  importData();
}
