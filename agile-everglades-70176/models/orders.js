const mongoose = require("mongoose");
const Schema = mongoose.Schema;
const orderSchema = mongoose.Schema({
  user: {
    type: Schema.Types.ObjectId,
    required: true,
    ref: "SwagatUsers",
  },
  orderItems: [
    {
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
    },
  ],

  branch: {
    type: Schema.Types.ObjectId,
    required: true,
    ref: "Branches",
  },
  totalPrice: {
    type: Number,
    required: true,
  },
  comments: {
    type: String,
    default: "No Comment"
  },
  isPaid: {
    type: Boolean,
    default: false,
  },
  status: {
    type: String,
    default: "Ordered"
  },
  date: {
    type: Date,
    dafault: Date.now,
  },
},
{
  timestamps: true
});

const Orders = mongoose.model("Orders", orderSchema);

module.exports = Orders;
