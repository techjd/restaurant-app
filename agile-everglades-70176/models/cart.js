const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const cartSchema = mongoose.Schema({
  user: {
    type: Schema.Types.ObjectId,
    required: true,
    ref: "SwagatUsers",
  },
  foodId: {
    type: Schema.Types.ObjectId,
    required: true,
    ref: "Foods",
  },
  itemName: {
    type: String,
    required: true,
  },
  price: {
    type: Number,
    required: true,
  },
  quantity: {
    type: Number,
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

const CartItems = mongoose.model("Cart", cartSchema);

module.exports = CartItems;
