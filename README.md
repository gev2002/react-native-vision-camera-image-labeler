
A plugin to label images using ML Kit Image Labeler. With High Performance.

# 🚨 Required Modules
react-native-vision-camera => 4.3.2 <br/>
react-native-worklets-core = 1.3.3

## 💻 Installation

```sh
npm install react-native-vision-camera-image-labeler
yarn add react-native-vision-camera-image-labeler
```
## 👷Features
    Easy To Use.
    Works Just Writing few lines of Code.
    Works With React Native Vision Camera.
    Works for Both Cameras.
    Works Fast.
    Works With Android 🤖 and IOS.📱
    Writen With Kotlin and Swift.
    Can Scan Image From Photo. 📸

## 💡 Usage

```js
import React, { useState } from 'react'
import { useCameraDevice } from 'react-native-vision-camera'
import { Camera } from 'react-native-vision-camera-image-labeler';

function App (){
  const [data,setData] = useState(null)
  const device = useCameraDevice('back');
  console.log(data)
  return(
    <>
      {!!device && (
        <Camera
          style={StyleSheet.absoluteFill}
          device={device}
          isActive
          // optional
          options={{
            minConfidence: 0.1
          }}
          callback={(d) => setData(d)}
        />
      )}
    </>
  )
}

```
### Also You Can Use Like This

```js
import React from 'react';
import { StyleSheet } from "react-native";
import {
  Camera,
  useCameraDevice,
  useFrameProcessor,
} from "react-native-vision-camera";
import { useImageLabeler } from "react-native-vision-camera-image-labeler";

function App() {
  const device = useCameraDevice('back');
  const options = {minConfidence : 0.1}
  const {labelerImage} = useImageLabeler(options)
  const frameProcessor = useFrameProcessor((frame) => {
    'worklet'
    const data = labelerImage(frame)
	console.log(data, 'data')
  }, [])
  return (
      <>
      {!!device && (
        <Camera
          style={StyleSheet.absoluteFill}
          device={device}
          isActive
          frameProcessor={frameProcessor}
        />
      )}
      </>
  );
}
export default App;
```


---

## ⚙️ Options

| Name |  Type    |  Values   | Default |
| :---:   | :---: |:---------:|  :---: |
| minConfidence | Number   | 0.1 - 1.0 | 0.1 |


##  Scan From Photo 📸

```js
import { ImageScanner } from "react-native-vision-camera-image-labeler";

const result = await ImageScanner({
    uri:assets.uri,
    orientation: "portrait",
    minConfidence: 1.0

})
console.log(result);

```
<h4>🚨 Orientation available only for iOS, recommendation give it when you are using Camera.</h3>

|    Name     |  Type  |                           Values                            | Required | Default  |   Platform   |
|:-----------:|:------:|:-----------------------------------------------------------:|:--------:|:--------:|:------------:|
|     uri     | string |                                                             |   yes    |          | android, iOS |
| orientation | string | portrait, portraitUpsideDown, landscapeLeft, landscapeRight |    no    | portrait |     iOS      |
| minConfidence | number |                          0.1 ~ 1.0                          |    no    |   1.0    | android,iOS  |
















