package nl.muller.bedrijfsapp.user.controller

import nl.muller.bedrijfsapp.product.data.Product
import nl.muller.bedrijfsapp.product.model.CreateProductRequest
import nl.muller.bedrijfsapp.user.data.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class ProductController @Autowired constructor(private val productRepository: ProductRepository) {
    @GetMapping("/")
    fun list() = productRepository.findAll()

    @GetMapping("/{productId}")
    fun get(@PathVariable("productId") productId : Long) = productRepository.findById(productId)

    @PostMapping("/")
    @ResponseBody
    fun create(@RequestBody createRequest : CreateProductRequest) : ResponseEntity<Product> {
        var product = Product(createRequest.name, createRequest.points, createRequest.price, createRequest.imageUrl)
        product = productRepository.save(product)
        return ResponseEntity.status(HttpStatus.CREATED).body(product)
    }

    @PutMapping("/{productId}")
    fun delete(@PathVariable("productId") productId : Long, @RequestBody updateRequest : CreateProductRequest) : ResponseEntity<String> {
        var product = productRepository.findById(productId)

        if (!product.isPresent)
            return ResponseEntity.notFound().build()

        product.get().apply {
            name = updateRequest.name
            points = updateRequest.points
            price = updateRequest.price
            imageUrl = updateRequest.imageUrl
        }

        productRepository.save(product.get())

        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{productId}")
    fun delete(@PathVariable("productId") productId : Long) : ResponseEntity<String> {
        productRepository.deleteById(productId)
        return ResponseEntity.ok().build()
    }
}