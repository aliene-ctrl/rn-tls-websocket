
# rn-tls-websocket

## Getting started

`$ npm install @iboz/rn-tls-websocket --save`

### Mostly automatic installation

`$ react-native link @iboz/rn-tls-websocket`

### Manual installation

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.iboz.react.RNTLSWebSocketPackage;` to the imports at the top of the file
  - Add `new RNTLSWebSocketPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':rn-tls-websocket'
  	project(':rn-tls-websocket').projectDir = new File(rootProject.projectDir, 	'../node_modules/rn-tls-websocket/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':rn-tls-websocket')
  	```

#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `rn-tls-websocket` and add `RNUnsafeWebSocket.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNUnsafeWebSocket.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<


## Usage
```javascript
import RNTLSWebSocket from '@iboz/rn-tls-websocket';

// TODO: What to do with the module?
const ws = new RNTLSWebSocket(url, protocol);
```
  