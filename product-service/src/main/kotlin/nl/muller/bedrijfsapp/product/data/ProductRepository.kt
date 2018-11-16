package nl.muller.bedrijfsapp.user.data

import nl.muller.bedrijfsapp.product.data.Product
import org.springframework.data.repository.CrudRepository

interface ProductRepository : CrudRepository<Product, Long> {

}