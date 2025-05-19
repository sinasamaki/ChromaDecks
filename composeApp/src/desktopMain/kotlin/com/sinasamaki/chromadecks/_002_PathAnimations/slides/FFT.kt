package com.sinasamaki.chromadecks._002_PathAnimations.slides

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.TargetDataLine

/**
 * super unoptimized or there is a bug somewhere.
 * Either way, use with caution. This almost turned
 * my laptop to a helicopter
 *
 * If you know how to fix, plz help
 * */

const val SAMPLE_RATE = 44100
const val BUFFER_SIZE = 1024
val FORMAT = AudioFormat(
    SAMPLE_RATE.toFloat(), 16, 1, true, false
)

object FFT {
    fun fft(input: DoubleArray): ComplexArray {
        val n = input.size
        if (n <= 1) {
            return ComplexArray(doubleArrayOf(input[0]), doubleArrayOf(0.0))
        }

        val even = DoubleArray(n / 2)
        val odd = DoubleArray(n / 2)
        for (i in 0 until n / 2) {
            even[i] = input[2 * i]
            odd[i] = input[2 * i + 1]
        }

        val q = fft(even)
        val r = fft(odd)
        val outputReal = DoubleArray(n)
        val outputImag = DoubleArray(n)
        for (k in 0 until n / 2) {
            val t = Complex.fromPolar(1.0, -2 * Math.PI * k / n) * Complex(r.real[k], r.imag[k])
            outputReal[k] = q.real[k] + t.real
            outputImag[k] = q.imag[k] + t.imag
            outputReal[k + n / 2] = q.real[k] - t.real
            outputImag[k + n / 2] = q.imag[k] - t.imag
        }
        return ComplexArray(outputReal, outputImag)
    }

    data class Complex(val real: Double, val imag: Double) {
        operator fun plus(other: Complex): Complex {
            return Complex(real + other.real, imag + other.imag)
        }

        operator fun minus(other: Complex): Complex {
            return Complex(real - other.real, imag - other.imag)
        }

        operator fun times(other: Complex): Complex {
            return Complex(
                real * other.real - imag * other.imag,
                real * other.imag + imag * other.real
            )
        }

        companion object {
            fun fromPolar(magnitude: Double, angle: Double): Complex {
                return Complex(
                    magnitude * kotlin.math.cos(angle),
                    magnitude * kotlin.math.sin(angle)
                )
            }
        }
    }

    data class ComplexArray(val real: DoubleArray, val imag: DoubleArray)
}

fun getMicrophoneInputStream(): AudioInputStream? {
    try {
        val targetDataLine =
            AudioSystem.getLine(DataLine.Info(TargetDataLine::class.java, FORMAT)) as TargetDataLine
        targetDataLine.open()
        targetDataLine.start()
        return AudioInputStream(targetDataLine)
//        return AudioSystem.getAudioInputStream(targetDataLine)
    } catch (e: Exception) {
        println("Error capturing microphone audio: ${e.message}")
        return null
    }
}

fun processAudioData(audioInputStream: AudioInputStream, numBands: Int): Flow<DoubleArray> = flow {
    val buffer = ByteArray(BUFFER_SIZE)
    val doubleBuffer = DoubleArray(BUFFER_SIZE)
    println("processing ...")
    while (true) {
//        print(" ... ")
        val bytesRead = audioInputStream.read(buffer, 0, BUFFER_SIZE)
        if (bytesRead == -1) {
            println("End of audio stream reached")
            break
        }
        if (bytesRead == 0) continue

        for (i in 0 until bytesRead / 2) {
            val sample = (buffer[i * 2].toInt() and 0xFF) or (buffer[i * 2 + 1].toInt() shl 8)
            doubleBuffer[i] = sample / 32768.0
        }
        if (bytesRead < BUFFER_SIZE) {
            for (i in bytesRead / 2 until BUFFER_SIZE) {
                doubleBuffer[i] = 0.0
            }
        }

        for (i in doubleBuffer.indices) {
            doubleBuffer[i] *= 0.5 * (1 - kotlin.math.cos(2 * Math.PI * i / (BUFFER_SIZE - 1)))
        }

        val fftResult = FFT.fft(doubleBuffer)

        val magnitudes = DoubleArray(BUFFER_SIZE / 2)
        for (i in 0 until BUFFER_SIZE / 2) {
            magnitudes[i] =
                kotlin.math.sqrt(fftResult.real[i] * fftResult.real[i] + fftResult.imag[i] * fftResult.imag[i])
        }

        val bands = DoubleArray(numBands)
        val bandWidth = (BUFFER_SIZE / 2) / numBands

        for (i in 0 until numBands) {
            var sum = 0.0
            for (j in i * bandWidth until (i + 1) * bandWidth) {
                if (j < magnitudes.size) {
                    sum += magnitudes[j]
                }
            }
            bands[i] = if (bandWidth > 0) sum / bandWidth else 0.0
        }
        emit(bands)
    }
}.flowOn(Dispatchers.IO)
