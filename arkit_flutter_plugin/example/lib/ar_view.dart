import 'package:flutter/foundation.dart';
import 'package:flutter/widgets.dart';
import 'package:arkit_plugin/arkit_plugin.dart';


class ARViewBuilder {

  Widget build(BuildContext context) {
    if (defaultTargetPlatform == TargetPlatform.iOS) {
      return ARKitSceneView(
        showFeaturePoints: false,
        planeDetection: ARPlaneDetection.horizontal,
        detectionImagesGroupName: "AR Resources",
        showWorldOrigin: true,
        onARKitViewCreated: handleArViewCreated(),
      );
    } else {
      return Text('$defaultTargetPlatform is not supported by this plugin');
    }
  }


  handleArViewCreated() {

  }

  Future<List> getWordPosition(){
    return null;
  }
}
