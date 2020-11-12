const express = require("express");
const colors = require("colors");
const dotenv = require("dotenv");
const bodyParser = require("body-parser");
const connectDB = require("./config/db");
const foodRoutes = require("./routes/foodRoutes");
const branchRoutes = require("./routes/branchRoutes");
const cartRoutes = require("./routes/cartRoutes");
const userRoutes = require("./routes/userRoutes");

const app = express();
app.use(express.json());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
dotenv.config();

connectDB();

app.get("/", (req, res) => {
  res.send("API IS RUNNING");
});

app.use("/api/allBranches", branchRoutes);
app.use("/api/allFoodItems", foodRoutes);
app.use("/api/allCartItems", cartRoutes);
app.use("/api/users", userRoutes);
const PORT = process.env.PORT || 5000;

app.listen(
  PORT,
  console.log(
    `Server Running on PORT ${PORT} in ${process.env.NODE_ENV} MODE `.yellow
  )
);
