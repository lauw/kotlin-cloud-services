package nl.muller.bedrijfsapp.product.data

import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "Product")
data class Product(
        @NotNull @Size(max = 50) var name : String,
        @NotNull var points : Int,
        var price : Double? = null,
        var imageUrl : String? = null,
        @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) val id: Long = 0) {

    fun calculatePrice(discountPercentage: Int) : Double {
        price?.let {
            return it - calculateDiscount(discountPercentage)
        }

        return 0.00
    }

    private fun calculateDiscount(discountPercentage : Int) : Double {
        price?.let {
            if (discountPercentage == 0)
                return 0.00

            return (discountPercentage / 100.00) * it
        }

        return 0.00
    }
}