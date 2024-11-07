package com.example.teste_mobile_wefit.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.teste_mobile_wefit.entity.CartEntity
import com.example.teste_mobile_wefit.entity.CartItemEntity

@Dao
interface CartDAO {
    @Query("SELECT id FROM cart WHERE finalized = 0 ORDER BY id DESC LIMIT 1")
    suspend fun getLastOpenCart(): Long?

    @Query("SELECT * FROM cart_item WHERE cartId = :cartId")
    suspend fun getItemsCartOpen(cartId: Long): List<CartItemEntity>?

    @Query("SELECT * FROM cart WHERE finalized = 0 ORDER BY id DESC LIMIT 1")
    suspend fun getCart(): CartEntity?

    @Insert
    suspend fun createCart(data: CartEntity): Long

    @Query(
        """
            UPDATE 
                cart 
            SET 
                total = (
                    SELECT 
                        SUM(quantity * price) 
                    FROM 
                        cart_item 
                    WHERE 
                        cartId = :cartId)
            WHERE 
                id = :cartId
        """
    )
    suspend fun updateTotalCart(cartId: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItemToCart(data: CartItemEntity): Long

    @Delete
    suspend fun removeItem(data: CartItemEntity)

    @Query("UPDATE cart SET finalized = 1, dateFinalized = :dateFinalized WHERE id = :cartId")
    suspend fun finishCart(cartId: Int, dateFinalized: String): Int
}