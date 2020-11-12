const mongoose = require("mongoose");

const itemSchema = mongoose.Schema({
  itemNo: {
    type: Number,
    required: true,
  },
  itemName: {
    type: String,
    required: true,
  },
  price: {
    type: Number,
    required: true,
  },
  stockORNot: {
    type: Boolean,
    required: true,
  },
  category: {
    type: String,
    required: true,
  },

  date: {
    type: Date,
    dafault: Date.now(),
  },
},
{
  timestamps: true
});

const FoodItems = mongoose.model("Foods", itemSchema);

module.exports = FoodItems;
