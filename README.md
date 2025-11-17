# vynilapp-misw4203

### Correr unit test

```cmd
./gradlew test
```
### Correr Test E2E

```cmd
./gradlew :app:connectedDebugAndroidTest
```

### Enlace a la apk pre-construida

Descarga una apk en version debug de la aplicación [aquí](./apks/semana-6/app-debug.apk)

### Build an apk

```cmd
./gradlew assembleDebug
```

### APK location:

```cmd
app/build/outputs/apk/debug/app-debug.apk
```


## 📱 Ejecutar la Aplicación

### Opción 1: Usando Android Studio

1. **Abrir el proyecto**:
   ```bash
   # Abre Android Studio y selecciona "Open"
   # Navega a la carpeta del proyecto
   ```

2. **Sincronizar dependencias**:
   - Click en `File` → `Sync Project with Gradle Files`
   - Espera a que termine la sincronización

3. **Seleccionar dispositivo**:
   - En la barra superior, click en el dropdown de dispositivos
   - Selecciona un emulador o dispositivo físico conectado

4. **Ejecutar la aplicación**:
   - Click en el botón `Run` ▶️ (o presiona `Shift + F10`)
   - La app se instalará y ejecutará automáticamente

---

### Opción 2: Usando Línea de Comandos

#### **En Emulador**

**Paso 1**: Inicia el emulador
```bash
# Lista los emuladores disponibles
emulator -list-avds

# Inicia un emulador (reemplaza 'Pixel_5_API_33' con tu AVD)
emulator -avd Pixel_5_API_33 &
```

**Paso 2**: Construye e instala
```bash
# Limpia, construye e instala en un solo comando
./gradlew clean assembleDebug installDebug

# O instala el APK ya generado
adb install app/build/outputs/apk/debug/app-debug.apk
```

**Paso 3**: Inicia la aplicación
```bash
adb shell am start -n com.uniandes.vynilapp/.MainActivity
```

---

#### **En Dispositivo Real**

**Paso 1**: Habilita las opciones de desarrollador en tu dispositivo
1. Ve a `Ajustes` → `Acerca del teléfono`
2. Toca 7 veces en `Número de compilación`
3. Ve a `Ajustes` → `Opciones de desarrollador`
4. Activa `Depuración USB`

**Paso 2**: Conecta el dispositivo por USB
```bash
# Verifica que el dispositivo esté conectado
adb devices

# Deberías ver algo como:
# List of devices attached
# ABC123XYZ    device
```

**Paso 3**: Instala la aplicación
```bash
# Método 1: Construye e instala automáticamente
./gradlew installDebug

# Método 2: Instala APK manualmente
adb install app/build/outputs/apk/debug/app-debug.apk

# Método 3: Con reemplazo (si ya está instalada)
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

**Paso 4**: Inicia la aplicación
```bash
adb shell am start -n com.uniandes.vynilapp/.MainActivity
```

---

### Opción 3: Instalación Manual del APK

**Paso 1**: Genera el APK
```bash
./gradlew assembleDebug
```

**Paso 2**: Transfiere el APK al dispositivo
- Copia `app/build/outputs/apk/debug/app-debug.apk` a tu dispositivo
- Puedes usar email, USB, Google Drive, etc.

**Nota:** Tambien tenemos una version publica pre construida disponible en este [enlace](./app-debug.apk)

**Paso 3**: Instala desde el dispositivo
1. Abre el gestor de archivos en tu dispositivo
2. Navega a donde copiaste el APK
3. Toca el archivo APK
4. Si es necesario, habilita "Instalar apps desconocidas" para el gestor de archivos
5. Toca `Instalar`

---

## 📊 Verificar la Instalación

```bash
# Ver logs de la aplicación
adb logcat | grep VynilApp

# Ver información de la app instalada
adb shell dumpsys package com.uniandes.vynilapp | grep version

# Forzar cierre de la app
adb shell am force-stop com.uniandes.vynilapp

# Reiniciar la app
adb shell am start -n com.uniandes.vynilapp/.MainActivity
```

---

## 🎯 Comandos Rápidos

| Comando | Descripción |
|---------|-------------|
| `./gradlew assembleDebug` | Genera APK de debug |
| `./gradlew installDebug` | Construye e instala en dispositivo |
| `adb install [apk]` | Instala APK manualmente |
| `adb uninstall com.uniandes.vynilapp` | Desinstala la app |
| `adb devices` | Lista dispositivos conectados |
| `adb shell am start -n com.uniandes.vynilapp/.MainActivity` | Inicia la app |

---

## 💡 Recomendaciones

- ✅ **Para desarrollo**: Usa `./gradlew installDebug` (más rápido)
- ✅ **Para testing**: Usa Android Studio Run (mejor debugging)
- ✅ **Para distribución interna**: Usa el APK debug pre-compilado que te dejamos referenciado en este documento
