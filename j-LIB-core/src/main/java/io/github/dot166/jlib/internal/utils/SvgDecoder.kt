package io.github.dot166.jlib.internal.utils

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.ResourceDecoder
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.resource.SimpleResource
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGParseException
import java.io.IOException
import java.io.InputStream

/** Decodes an SVG internal representation from an [InputStream].  */
class SvgDecoder : ResourceDecoder<InputStream?, SVG?> {
    override fun handles(source: InputStream, options: Options): Boolean {
        // TODO: Can we tell?
        return true
    }

    @Throws(IOException::class)
    override fun decode(
        source: InputStream, width: Int, height: Int, options: Options
    ): Resource<SVG?>? {
        try {
            // this is the implementation from lib v4.10 working w/o issues in RecyclerView
            val svg = SVG.getFromInputStream(source)

            // region these lines were added in v4.12 but cause issues with RecyclerView
            //if (width != Target.SIZE_ORIGINAL) {
            //    svg.setDocumentWidth(width.toFloat())
            //}
            //if (height != Target.SIZE_ORIGINAL) {
            //    svg.setDocumentHeight(height.toFloat())
            //}
            // endregion

            return SimpleResource<SVG?>(svg)
        } catch (ex: SVGParseException) {
            throw IOException("Cannot load SVG from stream", ex)
        }
    }
}