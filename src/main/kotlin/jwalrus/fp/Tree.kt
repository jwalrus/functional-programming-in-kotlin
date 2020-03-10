package jwalrus.fp

sealed class Tree<out A>

data class Leaf<A>(val value: A) : Tree<A>()

data class Branch<A>(val left: Tree<A>, val right: Tree<A>): Tree<A>()


// exercise 3.24
fun <A> size(tree: Tree<A>): Int = when (tree) {
    is Leaf -> 1
    is Branch -> 1 + size(tree.left) + size(tree.right)
}

// exercise 3.25
fun maximum(tree: Tree<Int>): Int = when (tree) {
    is Leaf -> tree.value
    is Branch -> maxOf(maximum(tree.left), maximum(tree.right))
}

// exercise 3.26
fun <A> depth(tree: Tree<A>): Int = when (tree) {
    is Leaf -> 1
    is Branch -> 1 + maxOf(depth(tree.left), depth(tree.right))
}

// exercise 3.27
fun <A, B> map(tree: Tree<A>, f: (A) -> B): Tree<B> = when (tree) {
    is Leaf -> Leaf(f(tree.value))
    is Branch -> Branch(map(tree.left, f), map(tree.right, f))
}

// exercise 3.28
fun <A, B> fold(ta: Tree<A>, l: (A) -> B, b: (B, B) -> B): B = when (ta) {
    is Leaf -> l(ta.value)
    is Branch -> b(fold(ta.left, l, b), fold(ta.right, l, b))
}

fun <A> sizeF(tree: Tree<A>): Int = fold(tree, { 1 }, { l, r -> 1 + l + r })

fun maximumF(tree: Tree<Int>): Int = fold(tree, { it }, { l, r -> maxOf(l, r)})

fun <A> depthF(tree: Tree<A>): Int = fold(tree, { 1 }, { l, r -> 1 + maxOf(l, r)})

fun <A, B> mapF(tree: Tree<A>, f: (A) -> B): Tree<B>
        = fold(tree, { a: A -> Leaf(f(a)) }, { b1: Tree<B>, b2: Tree<B> -> Branch(b1, b2)})