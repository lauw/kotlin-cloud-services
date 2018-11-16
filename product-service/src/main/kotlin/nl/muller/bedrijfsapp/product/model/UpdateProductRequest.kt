package nl.muller.bedrijfsapp.product.model

data class UpdateProductRequest(val id : Long, val name : String = "", val points : Int = 0, var price : Double? = null, var imageUrl : String? = null)
