package io.github.dot166.jlib.app.crashpad

/**
 * Public typealias for [java.io.File]. As a file object can either be a file
 * or a directory, this typealias allows you to be more verbose on what you
 * expect, in this case a file system directory.
 */
typealias FsDir = java.io.File

/**
 * Public typealias for [java.io.File]. As a file object can either be a file
 * or a directory, this typealias allows you to be more verbose on what you
 * expect, in this case a file system file.
 */
typealias FsFile = java.io.File

@Suppress("NOTHING_TO_INLINE")
inline fun FsDir.subDir(relPath: String) = FsDir(this, relPath)

@Suppress("NOTHING_TO_INLINE")
inline fun FsDir.subFile(relPath: String) = FsFile(this, relPath)