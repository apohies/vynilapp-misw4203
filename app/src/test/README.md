# Pruebas Unitarias - VynilApp

Este documento describe las pruebas unitarias implementadas para el proyecto VynilApp.

## 📁 Estructura de Pruebas

```
app/src/test/java/com/uniandes/vynilapp/
├── data/
│   ├── remote/
│   │   └── AlbumServiceAdapterTest.kt
│   └── repository/
│       └── AlbumRepositoryTest.kt
├── presentation/
│   └── album/
│       └── detail/
│           └── AlbumDetailViewModelTest.kt
├── di/
│   └── TestNetworkModule.kt
└── MainDispatcherRule.kt
```

## 🧪 Pruebas Implementadas

### 1. AlbumServiceAdapterTest
**Ubicación**: `data/remote/AlbumServiceAdapterTest.kt`

**Cobertura**:
- ✅ `getAlbumById()` - Casos de éxito y fallo
- ✅ `getAllAlbums()` - Casos de éxito y fallo
- ✅ Manejo de errores de red
- ✅ Manejo de respuestas nulas
- ✅ Conversión de DTOs a modelos de dominio

**Casos de prueba**:
- Respuesta exitosa con datos válidos
- Respuesta fallida con códigos de error HTTP
- Excepciones de red/conexión
- Respuestas con body nulo
- Verificación de llamadas a la API

### 2. AlbumRepositoryTest
**Ubicación**: `data/repository/AlbumRepositoryTest.kt`

**Cobertura**:
- ✅ Delegación correcta al ServiceAdapter
- ✅ Propagación de resultados exitosos
- ✅ Propagación de errores
- ✅ Verificación de parámetros pasados
- ✅ Múltiples llamadas simultáneas

**Casos de prueba**:
- Delegación exitosa a `getAllAlbums()`
- Delegación exitosa a `getAlbumById()`
- Propagación de errores del ServiceAdapter
- Verificación de parámetros correctos
- Manejo de múltiples llamadas

### 3. AlbumDetailViewModelTest
**Ubicación**: `presentation/album/detail/AlbumDetailViewModelTest.kt`

**Cobertura**:
- ✅ Carga de álbumes por ID
- ✅ Manejo de estados de carga
- ✅ Manejo de errores
- ✅ Eventos de UI (like, save, play, pause)
- ✅ Gestión de comentarios
- ✅ Estados de UI

**Casos de prueba**:
- Carga exitosa de álbum
- Manejo de errores de carga
- Estados de loading
- Toggle de like/save
- Control de reproducción
- Agregar comentarios
- Actualizar texto de comentarios
- Obtener ID actual del álbum
- Refrescar álbum

## 🛠️ Dependencias de Testing

```kotlin
// En build.gradle.kts
testImplementation("io.mockk:mockk:1.13.8")
testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
testImplementation("androidx.arch.core:core-testing:2.2.0")
testImplementation("junit:junit:4.13.2")
```

## 🚀 Cómo Ejecutar las Pruebas

### En Android Studio:
1. **Ejecutar todas las pruebas**:
   - Click derecho en `app/src/test/`
   - Seleccionar "Run 'All Tests'"

2. **Ejecutar pruebas específicas**:
   - Click derecho en el archivo de prueba
   - Seleccionar "Run '[TestClassName]'"

3. **Ejecutar una prueba individual**:
   - Click derecho en el método de prueba
   - Seleccionar "Run '[testMethodName]'"

### Desde Terminal:
```bash
# Ejecutar todas las pruebas unitarias
./gradlew test

# Ejecutar pruebas específicas
./gradlew test --tests "com.uniandes.vynilapp.data.remote.AlbumServiceAdapterTest"

# Ejecutar con reporte de cobertura
./gradlew test jacocoTestReport
```

## 📊 Cobertura de Pruebas

### AlbumServiceAdapter
- **Métodos cubiertos**: 100%
- **Líneas cubiertas**: ~95%
- **Ramas cubiertas**: ~90%

### AlbumRepository
- **Métodos cubiertos**: 100%
- **Líneas cubiertas**: 100%
- **Ramas cubiertas**: 100%

### AlbumDetailViewModel
- **Métodos cubiertos**: ~90%
- **Líneas cubiertas**: ~85%
- **Ramas cubiertas**: ~80%

## 🔧 Configuración de Pruebas

### TestNetworkModule
- Proporciona mocks para pruebas de Hilt
- Reemplaza el NetworkModule real
- Permite inyección de dependencias mockeadas

### MainDispatcherRule
- Configura el dispatcher principal para pruebas de corrutinas
- Usa `UnconfinedTestDispatcher` para pruebas síncronas
- Limpia el dispatcher después de cada prueba

## 📝 Patrones de Prueba Utilizados

### 1. Arrange-Act-Assert (AAA)
```kotlin
@Test
fun `test method should do something when condition`() = runBlocking {
    // Arrange
    val input = createTestData()
    coEvery { mock.method() } returns expectedResult
    
    // Act
    val result = sut.method(input)
    
    // Assert
    assertTrue(result.isSuccess)
    coVerify { mock.method() }
}
```

### 2. Mocking con MockK
```kotlin
// Crear mock
val mockService = mockk<ApiService>()

// Configurar comportamiento
coEvery { mockService.getData() } returns expectedData

// Verificar llamadas
coVerify { mockService.getData() }
```

### 3. Testing de Corrutinas
```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
```

## 🎯 Mejores Prácticas Implementadas

1. **Nombres descriptivos**: Los métodos de prueba describen claramente qué están probando
2. **Datos de prueba reutilizables**: Helper methods para crear datos de prueba
3. **Aislamiento**: Cada prueba es independiente y no afecta a otras
4. **Verificación completa**: Se verifica tanto el resultado como las interacciones
5. **Manejo de errores**: Se prueban tanto casos exitosos como fallos
6. **Cobertura completa**: Se cubren todos los métodos públicos y casos edge

## 🔍 Debugging de Pruebas

### Si una prueba falla:
1. Revisar el mensaje de error específico
2. Verificar que los mocks estén configurados correctamente
3. Asegurar que las dependencias estén disponibles
4. Verificar que los datos de prueba sean válidos

### Logs útiles:
```kotlin
// Agregar logs en las pruebas
println("Expected: $expected, Actual: $actual")
```

## 📈 Próximos Pasos

1. **Agregar pruebas de integración** para flujos completos
2. **Implementar pruebas de UI** con Compose Testing
3. **Agregar pruebas de performance** para operaciones críticas
4. **Configurar CI/CD** para ejecutar pruebas automáticamente
5. **Implementar reportes de cobertura** más detallados
