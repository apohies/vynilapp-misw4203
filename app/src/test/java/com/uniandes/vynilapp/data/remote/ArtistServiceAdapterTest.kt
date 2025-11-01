package com.uniandes.vynilapp.data.remote

import com.uniandes.vynilapp.model.dto.AlbumDto
import com.uniandes.vynilapp.model.dto.ArtistDto
import io.mockk.*

import com.uniandes.vynilapp.model.network.ApiService
import com.uniandes.vynilapp.model.services.ArtistServiceAdapter
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class ArtistServiceAdapterTest {

    private lateinit var apiService: ApiService
    private lateinit var artistServiceAdapter: ArtistServiceAdapter

    @Before
    fun setup() {
        apiService = mockk()
        artistServiceAdapter = ArtistServiceAdapter(apiService)
    }

    @Test
    fun `getArtistById should return success when API call is successful`() = runBlocking {

        val artistId = 1
        val artistDto = getArtistMock(1)
        val response = mockk<Response<ArtistDto>>()

        every { response.isSuccessful } returns true
        every { response.body() } returns artistDto
        coEvery { apiService.getArtistById(artistId) } returns response


        val result = artistServiceAdapter.getArtistById(artistId)


        assertTrue(result.isSuccess)
        val artist = result.getOrNull()
        assertNotNull(artist)
        assertEquals(artistDto.id, artist?.id)
        assertEquals(artistDto.name, artist?.name)
        assertEquals(artistDto.image, artist?.image)
        assertEquals(artistDto.birthDate, artist?.birthDate)
        assertEquals(artistDto.description, artist?.description)
        assertEquals(artistDto.albums, artist?.albums)
        assertEquals(artistDto.performerPrizes, artist?.performerPrizes)

        // Verify API call
        coVerify { apiService.getArtistById(artistId) }
    }

    @Test
    fun `getArtists should return success when API call is successful`() = runBlocking {

        val artistId = 1
        val artistsDtos = getArtistsMocks()
        val response = mockk<Response<List<ArtistDto>>>()

        every { response.isSuccessful } returns true
        every { response.body() } returns artistsDtos
        coEvery { apiService.getAllArtists() } returns response


        val result = artistServiceAdapter.getAllArtists()


        assertTrue(result.isSuccess)
        val artists = result.getOrNull()
        assertNotNull(artists)
        val artist0 = artists?.get(0)
        assertEquals(artistsDtos[0].id, artist0?.id)
        assertEquals(artistsDtos[0].name, artist0?.name)
        assertEquals(artistsDtos[0].image, artist0?.image)
        assertEquals(artistsDtos[0].birthDate, artist0?.birthDate)
        assertEquals(artistsDtos[0].description, artist0?.description)
        assertEquals(artistsDtos[0].albums, artist0?.albums)
        assertEquals(artistsDtos[0].performerPrizes, artist0?.performerPrizes)

        val artist1 = artists?.get(1)
        assertEquals(artistsDtos[1].id, artist1?.id)
        assertEquals(artistsDtos[1].name, artist1?.name)
        assertEquals(artistsDtos[1].image, artist1?.image)
        assertEquals(artistsDtos[1].birthDate, artist1?.birthDate)
        assertEquals(artistsDtos[1].description, artist1?.description)
        assertEquals(artistsDtos[1].albums, artist1?.albums)
        assertEquals(artistsDtos[1].performerPrizes, artist1?.performerPrizes)

        // Verify API call
        coVerify { apiService.getAllArtists() }
    }



    fun getArtistsMocks(): List<ArtistDto> {
        val artist1 = ArtistDto(
            id = 1,
            name = "Alvaro Diaz",
            image = "https://example.com/cover.jpg",
            birthDate = "1984-08-01T00:00:00.000Z",
            description = "Artista puertorriqueño de música urbana",
            albums = emptyList(),
            performerPrizes = emptyList()
        )

        val artist2 = ArtistDto(
            id = 2,
            name = "Frankie Ruíz",
            image = "https://example.com/cover.jpg",
            birthDate = "1958-03-10T00:00:00.000Z",
            description = "Fue un músico, cantante, compositor y director musical estadounidense de origen puertorriqueño.",
            albums = emptyList(),
            performerPrizes = emptyList()
        )

        val listOfArtists = listOf<ArtistDto>(artist1, artist2)
        return listOfArtists
    }

    fun getArtistMock(index: Int): ArtistDto {
        return getArtistsMocks()[index]
    }


}