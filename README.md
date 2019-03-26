
# react-native-unsafe-web-socket

## Getting started

`$ npm install @iboz/react-native-unsafe-web-socket --save`

### Mostly automatic installation

`$ react-native link @iboz/react-native-unsafe-web-socket`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-unsafe-web-socket` and add `RNUnsafeWebSocket.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNUnsafeWebSocket.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNUnsafeWebSocketPackage;` to the imports at the top of the file
  - Add `new RNUnsafeWebSocketPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-unsafe-web-socket'
  	project(':react-native-unsafe-web-socket').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-unsafe-web-socket/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-unsafe-web-socket')
  	```

#### Windows
[Read it! :D](https://github.com/ReactWindows/react-native)

1. In Visual Studio add the `RNUnsafeWebSocket.sln` in `node_modules/react-native-unsafe-web-socket/windows/RNUnsafeWebSocket.sln` folder to their solution, reference from their app.
2. Open up your `MainPage.cs` app
  - Add `using Unsafe.Web.Socket.RNUnsafeWebSocket;` to the usings at the top of the file
  - Add `new RNUnsafeWebSocketPackage()` to the `List<IReactPackage>` returned by the `Packages` method


## Usage
```javascript
import RNUnsafeWebSocket from '@iboz/react-native-unsafe-web-socket';

// TODO: What to do with the module?
const ws = new RNUnsafeWebSocket(url, protocol);
```
  