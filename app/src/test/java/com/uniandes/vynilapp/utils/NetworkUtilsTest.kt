package com.uniandes.vynilapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class NetworkUtilsTest {

    private lateinit var context: Context
    private lateinit var connectivityManager: ConnectivityManager

    @Before
    fun setup() {
        context = mockk(relaxed = true)
        connectivityManager = mockk(relaxed = true)
        every { context.getSystemService(Context.CONNECTIVITY_SERVICE) } returns connectivityManager
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.M])
    fun `isNetworkAvailable returns true when network is connected on Android M+`() {
        // Given
        val network = mockk<Network>()
        val networkCapabilities = mockk<NetworkCapabilities>(relaxed = true)

        every { connectivityManager.activeNetwork } returns network
        every { connectivityManager.getNetworkCapabilities(network) } returns networkCapabilities
        every {
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } returns true
        every {
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } returns true

        // When
        val result = NetworkUtils.isNetworkAvailable(context)

        // Then
        assertTrue(result)
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.M])
    fun `isNetworkAvailable returns false when network is null on Android M+`() {
        // Given
        every { connectivityManager.activeNetwork } returns null

        // When
        val result = NetworkUtils.isNetworkAvailable(context)

        // Then
        assertFalse(result)
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.M])
    fun `isNetworkAvailable returns false when network capabilities is null on Android M+`() {
        // Given
        val network = mockk<Network>()
        every { connectivityManager.activeNetwork } returns network
        every { connectivityManager.getNetworkCapabilities(network) } returns null

        // When
        val result = NetworkUtils.isNetworkAvailable(context)

        // Then
        assertFalse(result)
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.M])
    fun `isNetworkAvailable returns false when internet capability is missing on Android M+`() {
        // Given
        val network = mockk<Network>()
        val networkCapabilities = mockk<NetworkCapabilities>(relaxed = true)

        every { connectivityManager.activeNetwork } returns network
        every { connectivityManager.getNetworkCapabilities(network) } returns networkCapabilities
        every {
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } returns false
        every {
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } returns true

        // When
        val result = NetworkUtils.isNetworkAvailable(context)

        // Then
        assertFalse(result)
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.M])
    fun `isNetworkAvailable returns false when validated capability is missing on Android M+`() {
        // Given
        val network = mockk<Network>()
        val networkCapabilities = mockk<NetworkCapabilities>(relaxed = true)

        every { connectivityManager.activeNetwork } returns network
        every { connectivityManager.getNetworkCapabilities(network) } returns networkCapabilities
        every {
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } returns true
        every {
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } returns false

        // When
        val result = NetworkUtils.isNetworkAvailable(context)

        // Then
        assertFalse(result)
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.LOLLIPOP])
    @Suppress("DEPRECATION")
    fun `isNetworkAvailable returns true when network is connected on legacy Android`() {
        // Given
        val networkInfo = mockk<NetworkInfo>(relaxed = true)
        every { connectivityManager.activeNetworkInfo } returns networkInfo
        every { networkInfo.isConnected } returns true

        // When
        val result = NetworkUtils.isNetworkAvailable(context)

        // Then
        assertTrue(result)
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.LOLLIPOP])
    @Suppress("DEPRECATION")
    fun `isNetworkAvailable returns false when network info is null on legacy Android`() {
        // Given
        every { connectivityManager.activeNetworkInfo } returns null

        // When
        val result = NetworkUtils.isNetworkAvailable(context)

        // Then
        assertFalse(result)
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.LOLLIPOP])
    @Suppress("DEPRECATION")
    fun `isNetworkAvailable returns false when network is disconnected on legacy Android`() {
        // Given
        val networkInfo = mockk<NetworkInfo>(relaxed = true)
        every { connectivityManager.activeNetworkInfo } returns networkInfo
        every { networkInfo.isConnected } returns false

        // When
        val result = NetworkUtils.isNetworkAvailable(context)

        // Then
        assertFalse(result)
    }
}