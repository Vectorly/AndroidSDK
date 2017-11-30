package io.dotlearn.lrnplayer.utils

import org.junit.Test

import org.junit.Assert.*

class DisplayUtilsTest {

    @Test
    fun calculatesCorrectHeightOnCommonWidthsWith169AspectRatio() {
        assertEquals(DisplayUtils.calculateHeightBasedOnWidthAndAspectRatio(1.77777778,
                1280), 720)
        assertEquals(DisplayUtils.calculateHeightBasedOnWidthAndAspectRatio(1.77777778,
                1600), 900)
        assertEquals(DisplayUtils.calculateHeightBasedOnWidthAndAspectRatio(1.77777778,
                1920), 1080)
        assertEquals(DisplayUtils.calculateHeightBasedOnWidthAndAspectRatio(1.77777778,
                2560), 1440)
        assertEquals(DisplayUtils.calculateHeightBasedOnWidthAndAspectRatio(1.77777778,
                3840), 2160)
    }

}