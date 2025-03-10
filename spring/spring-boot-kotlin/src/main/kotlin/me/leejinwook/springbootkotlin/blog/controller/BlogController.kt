package me.leejinwook.springbootkotlin.blog.controller

import jakarta.validation.Valid
import me.leejinwook.springbootkotlin.blog.dto.BlogDto
import me.leejinwook.springbootkotlin.blog.entity.Wordcount
import me.leejinwook.springbootkotlin.blog.service.BlogService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/blog")
@RestController
class BlogController(
    val blogService: BlogService
) {
    @GetMapping("")
    fun search(@RequestBody @Valid blogDto: BlogDto): String? {
        val result = blogService.searchKakao(blogDto)
        return result
    }

    @GetMapping("/rank")
    fun searchWordRank(): List<Wordcount> = blogService.searchWordRank()
}