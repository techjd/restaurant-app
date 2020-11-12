const express = require("express");
const Cart = require("../models/cart");
const Users = require("../models/users");
const Orders = require("../models/orders");
const Foods = require("../models/items");
const dotenv = require("dotenv");
const auth = require("../middleware/auth");
const { OAuth2Client } = require("google-auth-library");
const client = new OAuth2Client(process.env.CLIENT_ID);
var fcm = require("fcm-node");
const FCM = require("fcm-node/lib/fcm");
const Branches = require("../models/branches");
const API_KEY =
  "AAAAZIQjllY:APA91bF4NXvKYSRy56I2OppEAtIE8kPv9fcdJakLdvZ74tVgWuNTEg08bLH9K5dBh8aOkY7QDFK-M5LkJCk1917BS28G2q9vmqAlJQt_BsiV_671_ZB_rWGxtOxBNZtdYL_x84KWDK56";
var fcm = new FCM(API_KEY);
const router = express.Router();

// Add a new Item to Cart
router.post("/", auth, async (req, res) => {
  const { foodId, itemName, price, quantity } = req.body;

  const userinfo = await Users.findById(req.user.id).select("-password");
  const id = userinfo.id;


  try {

    

    const newItem = new Cart({
      user: id,
      foodId,
      itemName,
      price,
      quantity,
    });

    await newItem.save();

    res.json(newItem);
    // console.log(newItem);
  } catch (error) {
    console.error(error.message);
    res.status(500).send("Server Error");
  }
});

// Get All Items in Cart

router.get("/", auth, async (req, res) => {


  const userinfo = await Users.findById(req.user.id).select("-password");
  const id = userinfo.id;

  const cartItems = await Cart.find({ user: id });
  // console.log(cartItems);

  const foodItem = cartItems.foodId;
  let totalPrice = 0;
  for (let j = 0; j < cartItems.length; j++) {
    // console.log(cartItems[j].foodId);

    const foodId = await Foods.findById(cartItems[j].foodId);
    // console.log(foodId.price);
    // console.log(cartItems[j].quantity);
    totalPrice += foodId.price * cartItems[j].quantity;
    // totalPrice += parseInt(foodId.price);
    // console.log(totalPrice);
    // console.log(cartItems[j].quantity);
  }
  console.log(totalPrice);
  // console.log(foodItem);
  // let total = 0;
  // for (let i = 0; i < cartItems.length; i++) {
  //   total += parseInt(cartItems[i].price);
  // }
  // console.log(total);
  res.json(cartItems);
});

// Delete All Items From Cart

router.delete("/deleteItems", auth  ,  async (req, res) => {


  const userinfo = await Users.findById(req.user.id).select("-password");
  const id = userinfo.id;

  const cartItems = await Cart.remove({ user: id });

  res.json({ msg: "All Items Successfully Deleted" });
});

// Add Items To Orders

router.post("/addToOrders", auth ,async (req, res) => {


  const userinfo = await Users.findById(req.user.id).select("-password");
  const id = userinfo.id;

  const cartItems = await Cart.find({ user: id });
  let total = 0;
  for (let i = 0; i < cartItems.length; i++) {
    total += parseInt(cartItems[i].price);
  }
  // console.log(total);
  // console.log(cartItems);
  const { branch } = req.body;

  const newOrder = new Orders({
    user: id,
    branch,
    orderItems: cartItems,
    totalPrice: total,
  });

  await newOrder.save();

  // res.json(newOrder);
  res.json(newOrder);
  console.log(newOrder);

  console.log(newOrder.isPaid);
  console.log(newOrder._id);
});

// Get order by id
router.get("/getOrdersforUsers/:id", async (req, res) => {
  const order = await Orders.findById(req.params.id);

  res.json(order);
});

router.get("/getOrders/:branchid", async (req, res) => {
  const branchid = req.params.branchid;
  console.log(branchid);
  const orders = await Orders.find({ branch: req.params.branchid });

  res.json(orders);
});

// Update the the paid value to true
router.put("/update/:orderid", auth ,async (req, res) => {
  const orderId = req.params.orderid;
  console.log(orderId);



  const userinfo = await Users.findById(req.user.id).select("-password");
  const id = userinfo.id;

  const cartItems = await Cart.remove({ user: id });

  const update = await Orders.findById(req.params.orderid);
  // console.log(update)
  console.log(update.isPaid);
  update.isPaid = true;

  (await update).save();

  const branchToken = await Branches.findById(update.branch)
  console.log(branchToken)
  const message = {
    to:
      "ft9WrBuvTSa_kHOemu-8cc:APA91bFRLmEW2-V2HnOQwDj4A_1wQQKp4DaIxAxOLyVVpuaaPGBC0NJQOzzHdIuwgpb7j5mbi1SHSMUzBquV3Z4xE67gUGDNihqcAJPAE1xCNTL_A4DlPffwU65svJpgJy-5LIN0jmlX",
    collapse_key: "collapse",

    notification: {
      title: "New Order",
      body: "Please Check Fast . There is new Order .",
    },
  };

  fcm.send(message, function (err, messageID) {
    if (err) {
      console.log(err);
      console.log("Something has gone wrong!");
    } else {
      console.log("Sent with message ID: ", messageID);
    }
  });

  res.json({ msg: "Payment Successffly Done" });
});

// // Add Comment 
// router.put("/addComment/:id", auth, async(req, res) => {
//   const { comment } = req.body.comment

//   const userinfo = await Users.findById(req.user.id).select("-password");
//   const id = userinfo.id;


// })

// Get All Orders OF Users
router.get("/getOrder", auth ,async (req, res) => {


  const userinfo = await Users.findById(req.user.id).select("-password");
  const id = userinfo.id;

  // const orders = await Orders.find({ user: id });
  const orders = await Orders.find({ user: id }).sort('-createdAt');

  console.log(orders);

  res.json(orders);
});


// Edit Items in Cart
router.put("/editItem/:id", auth ,async (req, res) => {
  // const token = req.headers.token;

  const quantity = req.body.quantity;



  const userinfo = await Users.findById(req.user.id).select("-password");
  const id = userinfo.id;

  const cartItem = await Cart.findById(req.params.id);
  console.log(cartItem.quantity);
  const foodItem = await Foods.findById(cartItem.foodId);
  console.log(foodItem);
  cartItem.quantity = quantity;
  price = foodItem.price;
  cartItem.price = quantity * price;

  (await cartItem).save();
  // cartItem.quantity = quantity;
  // await cartItem.update();

  res.json({ msg: "Cart Item Updated Successfully" });
});

// @route DELETE
// @desc Delete Item in a Cart
// Delete Item in Cart
router.delete("/deleteItem/:id", auth ,async (req, res) => {


  const userinfo = await Users.findById(req.user.id).select("-password");
  const id = userinfo.id;

  const cartItem = await Cart.findByIdAndDelete(req.params.id);

  console.log(cartItem);
  res.json({ msg: "Item Deleted" });
});


module.exports = router;
