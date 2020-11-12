const express = require("express");
const auth = require("../middleware/auth");
const FoodItems = require("../models/items");
const Users = require("../models/users")
const router = express.Router();

// Get All Food Items
router.get("/", async (req, res) => {
  const foodItems = await FoodItems.find({});
  res.json(foodItems);
});

// Get A Food By ID
router.get("/:id", async (req, res) => {
  const foodItem = await FoodItems.findById(req.params.id);
  if (foodItem) {
    res.json(foodItem);
  } else {
    res.status(404).json({ msg: "Product Not Found" });
  }
});

// Get A Food By Category
router.get("/cat/:category", async (req, res) => {
  const foodItem = await FoodItems.find({ category: `${req.params.category}` });
  console.log(req.params.category);
  if (foodItem) {
    res.json(foodItem);
  } else {
    res.status(404).json({ msg: "Not Found" });
  }
});


// Get All Category
router.get("/categories/getAllCategory", async (req, res) => {
  const allCategories = await FoodItems.distinct("category");

  if (allCategories) {
    res.json({ category: allCategories });
  } else {
    res.status(404).json("Did'nt Found Anything");
  }
});

router.post("/addFoodItem", auth ,async (req, res) => {
  const { itemNumber ,itemName, price, stockORNot, category } = req.body

  try {
    const userinfo = await Users.findById(req.user.id).select("-password");
    const id = userinfo.id;
    const admin = userinfo.isSuperAdmin
  
    if (!admin) {
      res.status(401).json({ msg: "You are not allowed " })
    }
    else {
      const newFoodItem = new FoodItems({
        itemNumber,
        itemName,
        price,
        stockORNot,
        category
      });
    
      console.log(newFoodItem)

      await newFoodItem.save();

      res.json(newFoodItem)
    }
  } catch (error) {
    console.error(error)
  }





})

router.put("/editFoodItem/:foodId", auth, async(req, res)=> {
  const foodId = req.params.foodId
  console.log(foodId)

  try {
    const userinfo = await Users.findById(req.user.id).select("-password");
    const id = userinfo.id;
    const admin = userinfo.isSuperAdmin
  
    if (!admin) {
      res.status(401).json({ msg: "You are not allowed " })
    }
    else {
      const { itemName, price, stockORNot, category } = req.body
      // res.status(200).json({ msg: "Wohhoho You are Allowed" })

      const food = await FoodItems.findById(foodId)

      const newFoodItem = {
        itemName, price, stockORNot, category
      }
      // const updated = FoodItems.findOneAndUpdate(foodId, newFoodItem, {new: true})

      //  (await updated).save()
      await FoodItems.findByIdAndUpdate(foodId, newFoodItem)
      res.status(200).json(newFoodItem)
      // console.log(food)
    }



  } catch (error) {
    console.error(error)
    res.status(500).json({ msg: "Server Error" })
  }

  

})
module.exports = router;
