package br.com.zup.pix

import java.util.*

fun String.toUUID() = UUID.fromString(this)