package com.example.clothingmall.data

data class ExternalData(
    val posts: List<Post> = emptyList(),
    val comments: List<Comment> = emptyList(),
    val profile: Profile = Profile()
)

data class Post(
    val id: Int,
    val title: String
)

data class Comment(
    val id: Int,
    val body: String,
    val postId: Int
)

data class Profile(
    val name: String = ""
) 