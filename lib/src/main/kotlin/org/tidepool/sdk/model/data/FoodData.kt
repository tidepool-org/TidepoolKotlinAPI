package org.tidepool.sdk.model.data

// schema food.v1
data class FoodData(
	val brand: String? = null,
	val code: String? = null,
	val meal: Meal? = null,
	val mealOther: String? = null,
	val name: String? = null,
) : BaseData(DataType.food) {
	val amount: Nothing
		get() = TODO("schema \"amount.v1\" not implemented")
	val ingredients: Nothing
		get() = TODO("schema \"ingredientarray.v1\" not implemented")
	val nutrition: Nothing
		get() = TODO("schema \"nutrition.v1\" not implemented")
	enum class Meal {
		breakfast,
		lunch,
		dinner,
		snack,
		other,
	}
}