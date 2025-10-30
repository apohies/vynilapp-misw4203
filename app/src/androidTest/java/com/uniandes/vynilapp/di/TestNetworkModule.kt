package com.uniandes.vynilapp.di

import com.uniandes.vynilapp.model.Album
import com.uniandes.vynilapp.model.Comment
import com.uniandes.vynilapp.model.Performer
import com.uniandes.vynilapp.model.Track
import com.uniandes.vynilapp.model.network.ApiService
import com.uniandes.vynilapp.model.services.AlbumServiceAdapter
import com.uniandes.vynilapp.model.repository.AlbumRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.delay
import retrofit2.Response
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class]
)
object TestNetworkModule {

    @Provides
    @Singleton
    fun provideMockApiService(): ApiService {
        return object : ApiService {
            override suspend fun getAlbumById(id: Int): Response<com.uniandes.vynilapp.model.dto.AlbumDto> {
                delay(100)
                
                return when (id) {
                    100 -> {
                        val albumDto = com.uniandes.vynilapp.model.dto.AlbumDto(
                            id = 100,
                            name = "Buscando América",
                            cover = "https://example.com/cover.jpg",
                            releaseDate = "1984-08-01T00:00:00.000Z",
                            description = "Buscando América es el primer álbum de la banda de Rubén Blades y Seis del Solar lanzado en 1984.",
                            genre = "Salsa",
                            recordLabel = "Elektra",
                            tracks = listOf(
                                com.uniandes.vynilapp.model.dto.TrackDto(100, "Decisiones", "5:05"),
                                com.uniandes.vynilapp.model.dto.TrackDto(101, "Desapariciones", "6:29")
                            ),
                            performers = listOf(
                                com.uniandes.vynilapp.model.dto.PerformerDto(
                                    id = 100,
                                    name = "Rubén Blades",
                                    image = "https://example.com/performer.jpg",
                                    description = "Cantante panameño",
                                    birthDate = "1948-07-16T00:00:00.000Z"
                                )
                            ),
                            comments = listOf(
                                com.uniandes.vynilapp.model.dto.CommentDto(100, "Excelente álbum", 5)
                            )
                        )
                        Response.success(albumDto)
                    }
                    101 -> {
                        val albumDto = com.uniandes.vynilapp.model.dto.AlbumDto(
                            id = 101,
                            name = "Otro Álbum",
                            cover = "https://example.com/cover2.jpg",
                            releaseDate = "1985-01-01T00:00:00.000Z",
                            description = "Otro álbum de prueba",
                            genre = "Rock",
                            recordLabel = "Sony",
                            tracks = null,
                            performers = null,
                            comments = null
                        )
                        Response.success(albumDto)
                    }
                    -1 -> {
                        Response.error(404, okhttp3.ResponseBody.create(null, "Not Found"))
                    }
                    else -> {
                        Response.error(500, okhttp3.ResponseBody.create(null, "Internal Server Error"))
                    }
                }
            }

            override suspend fun getAllAlbums(): Response<List<com.uniandes.vynilapp.model.dto.AlbumDto>> {
                delay(100)
                val albums = listOf(
                    com.uniandes.vynilapp.model.dto.AlbumDto(
                        id = 100,
                        name = "Buscando América",
                        cover = "https://example.com/cover.jpg",
                        releaseDate = "1984-08-01T00:00:00.000Z",
                        description = "Un álbum clásico",
                        genre = "Salsa",
                        recordLabel = "Elektra",
                        tracks = null,
                        performers = null,
                        comments = null
                    )
                )
                return Response.success(albums)
            }

            override suspend fun createAlbum(album: com.uniandes.vynilapp.model.dto.AlbumDto): Response<com.uniandes.vynilapp.model.dto.AlbumDto> {
                delay(100)
                return Response.success(album)
            }

            override suspend fun updateAlbum(
                albumId: Int,
                album: com.uniandes.vynilapp.model.dto.AlbumDto
            ): Response<com.uniandes.vynilapp.model.dto.AlbumDto> {
                delay(100)
                return Response.success(album)
            }

            override suspend fun deleteAlbum(albumId: Int): Response<Unit> {
                delay(100)
                return Response.success(Unit)
            }
        }
    }

    @Provides
    @Singleton
    fun provideMockAlbumServiceAdapter(apiService: ApiService): AlbumServiceAdapter {
        return AlbumServiceAdapter(apiService)
    }

    @Provides
    @Singleton
    fun provideMockAlbumRepository(albumServiceAdapter: AlbumServiceAdapter): AlbumRepository {
        return AlbumRepository(albumServiceAdapter)
    }
}
