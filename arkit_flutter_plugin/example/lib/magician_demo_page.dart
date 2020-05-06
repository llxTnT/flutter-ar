import 'dart:async';
import 'dart:convert';
import 'dart:io';

import 'package:arkit_plugin/arkit_plugin.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/screenutil.dart';
import 'package:vector_math/vector_math_64.dart' as vector;
import 'dobot_magician_b_tool.dart';

class MagicianDemoPage extends StatefulWidget {
  @override
  MagicianDemoPageState createState() => MagicianDemoPageState();
}

class MagicianDemoPageState extends State<MagicianDemoPage> {
  ARKitController arkitController;
  bool planeAnchorFound = false;
  bool imageAnchorFound = false;
  bool itemInit = false;
  ARKitSceneView containerView;
  ARKitPlaneAnchor planeAnchor;
  ARKitImageAnchor imageAnchor;
  vector.Vector3 imagePos;
  MagicianConnectTool connectTool = MagicianConnectTool();

  ARKitPlane plane;
  vector.Vector3 planePos;
  ARKitNode connectNode;
  ARKitNode node;
  ARKitNode xNode;
  ARKitNode yNode;
  ARKitNode zNode;

  ARKitLine xAxis;
  ARKitLine yAxis;
  ARKitLine zAxis;

  double deviceX, deviceY, deviceZ, deviceR;

  ARKitSphere planeNodeSphere;
  ARKitBox boxItem;
  ARKitNode boxNode;

  String anchorId;
  String connectText = "connect";

  bool doPickUp = false;
  bool isConnect = false;
  Timer timer;

  String poseText = "magician pose:";

  @override
  void dispose() {
    arkitController?.dispose();
    timer?.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    ScreenUtil.init(context);
    return Scaffold(
      appBar: AppBar(
        title: const Text('Magician Demo'),
        backgroundColor: Colors.indigo,
      ),
      body: Stack(
        children: <Widget>[
          Container(
            child: containerView = ARKitSceneView(
              showFeaturePoints: false,
              planeDetection: ARPlaneDetection.horizontal,
              detectionImagesGroupName: "AR Resources",
              onARKitViewCreated: onARKitViewCreated,
              showWorldOrigin: true,
            ),
          ),
          Positioned(
            left: 300,
            right: 300,
            bottom: 20,
            child: RaisedButton(
                child: Text("$connectText"), onPressed: handleConnectBTNPress),
          ),
          Positioned(
              left: 50,
              width: 200,
              top: 30,
              child: Text("$poseText", style: TextStyle(color: Colors.white))),
          Positioned(
            right: 50,
            width: 200,
            top: 30,
            child: RaisedButton(
                child: Text("Move to top"), onPressed: handleDemoBTNPress),
          ),
          Positioned(
            right: 50,
            width: 200,
            top: 80,
            child: RaisedButton(
                child: Text("Pick up"), onPressed: handlePickUpPress),
          ),

          /* Positioned(
            left: 400,
            right: 400,
            top: 20,
            child: RaisedButton(
                child: Text("getPose"), onPressed: handleGetPose),
          ),
          Positioned(
            left: 400,
            right: 400,
            top: 60,
            child: RaisedButton(
                child: Text("ptpCmd"), onPressed: handlePTPCmd),
          )*/
        ],
      ),
    );
  }

  void onARKitViewCreated(ARKitController arkitController) {
    this.arkitController = arkitController;
    this.arkitController.onAddNodeForAnchor = _handleAddAnchor;
    //this.arkitController.onUpdateNodeForAnchor = _handleAnchorChange;
  }

  void _handleAddAnchor(ARKitAnchor anchor) {
    if (anchor is ARKitImageAnchor) {
      if (!imageAnchorFound) _addImageAchor(arkitController, anchor);
    } else if (anchor is ARKitPlaneAnchor) {
      if (!planeAnchorFound) _addPlane(arkitController, anchor);
    }
    /*  if (!(anchor is ARKitPlaneAnchor)) {
      return;
    }*/
    //
  }

  void _addPlane(ARKitController controller, ARKitPlaneAnchor anchor) {
    //  containerView.configuration.
    planeAnchor = anchor;
    anchorId = anchor.identifier;

    controller.getNodeWorldPosition(anchor.nodeName).then((e) {
      if (e != null) {
        print("plane node x:" +
            e.x.toStringAsFixed(5) +
            "  y:" +
            e.y.toStringAsFixed(5) +
            "   z:" +
            e.z.toStringAsFixed(5));
        planePos = e;
        planeAnchorFound = true;
      }
    });
    final sphere = ARKitSphere(
      materials: [
        ARKitMaterial(
          transparency: 1,
          diffuse: ARKitMaterialProperty(color: Colors.red),
        )
      ],
      radius: 0.01,
    );
    /*  plane = ARKitPlane(
      width: anchor.extent.x,
      height: anchor.extent.z,
      materials: [
        ARKitMaterial(
          transparency: 1,
          diffuse: ARKitMaterialProperty(color: Colors.red),
        )
      ],
    );*/


    xAxis = ARKitLine(
      fromVector: vector.Vector3(0, 0, 0),
      toVector: vector.Vector3(0.3, 0, 0),
      materials: [
        ARKitMaterial(
          transparency: 1,
          diffuse: ARKitMaterialProperty(color: Colors.green),
        )
      ],
    );

    yAxis = ARKitLine(
      fromVector: vector.Vector3(0, 0, 0),
      toVector: vector.Vector3(0, 0.3, 0),
      materials: [
        ARKitMaterial(
          transparency: 1,
          diffuse: ARKitMaterialProperty(color: Colors.yellow),
        )
      ],
    );

    zAxis = ARKitLine(
      fromVector: vector.Vector3(0, 0, 0),
      toVector: vector.Vector3(0, 0, 0.3),
      materials: [
        ARKitMaterial(
          transparency: 1,
          diffuse: ARKitMaterialProperty(color: Colors.lightBlue),
        )
      ],
    );
    node = ARKitNode(
      geometry: sphere,
      position: vector.Vector3(0, 0, 0),
      //position: vector.Vector3(anchor.center.x, anchor.center.y, anchor.center.z),
      //rotation: vector.Vector4(1, 0, 0, -math.pi / 2),
    );
    xNode = ARKitNode(
      geometry: xAxis,
    );
    yNode = ARKitNode(
      geometry: yAxis,
    );
    zNode = ARKitNode(
      geometry: zAxis,
    );
    controller.add(node, parentNodeName: anchor.nodeName);
    controller.add(xNode, parentNodeName: anchor.nodeName);
    controller.add(yNode, parentNodeName: anchor.nodeName);
    controller.add(zNode, parentNodeName: anchor.nodeName);
  }

  void _addImageAchor(
      ARKitController arkitController, ARKitImageAnchor anchor) {
    print("find image:" + anchor.referenceImageName);
    imageAnchor = anchor;
    anchorId = anchor.identifier;
    final sphere = ARKitSphere(
      materials: [
        ARKitMaterial(
          transparency: 1,
          diffuse: ARKitMaterialProperty(color: Colors.red),
        )
      ],
      radius: 0.003,
    );
    final imagePosition = anchor.transform.getColumn(3);
    final node = ARKitNode(
      geometry: sphere,
      position:
          vector.Vector3(imagePosition.x, imagePosition.y, imagePosition.z),
      eulerAngles: vector.Vector3.zero(),
    );
    arkitController.add(node);
    arkitController.getNodeWorldPosition(imageAnchor.nodeName).then((e1) {
      if (e1 != null) {
        imagePos = e1;
        imageAnchorFound = true;
        print("image node x:" +
            e1.x.toStringAsFixed(5) +
            "  y:" +
            e1.y.toStringAsFixed(5) +
            "   z:" +
            e1.z.toStringAsFixed(5));
        print("dis x:" +
            (imagePos.x - planePos.x).toStringAsFixed(4) +
            "  y:" +
            (imagePos.y - planePos.y).toStringAsFixed(4) +
            "  z:" +
            (imagePos.z - planePos.z).toStringAsFixed(4));
      }
    });
  }

  void handleConnectBTNPress() {
    print("connect BTN Press");
    if (!isConnect) {
      connectTool.doSearchDevice().then((resStr) {
        print("search device:" + resStr);
        if (resStr != "error") {
          connectTool.doConnectDevice(resStr).then(handledOnDeviceConnect);
        }
      });
    } else {
      timer?.cancel();
      connectTool.disConnectDevice().then(handleOnDeviceDisConnect);
    }
  }

  FutureOr handledOnDeviceConnect(bool value) {
    print("on connect value:" + value.toString());
    if (value) {
      connectText = "disconnect";
      isConnect = true;
      connectTool.ptpCmd(200, 70, -5,0);
      setState(() {
        startGetPoseFuture();
      });
    }
  }

  void startGetPoseFuture() {
    timer = Timer.periodic(const Duration(milliseconds: 1000), (timer) {
      connectTool.getPose().then((str) {
        print(str);
        poseText = "magician pose:" + str;
        final poseJson = json.decode(str);
        Map<String, dynamic> pose = poseJson;

        double oldZ;
        if (doPickUp) {
          oldZ = deviceZ;
        }
        deviceX = pose["x"];
        deviceY = pose["y"];
        deviceZ = pose["z"];
        deviceR = pose["r"];
        if (doPickUp) {
          oldZ = deviceZ - oldZ;
          oldZ=oldZ/1000;
          final oldPos = boxNode.position.value;
          print("move y:"+oldZ.toStringAsFixed(5));
          boxNode.position.value =
              vector.Vector3(oldPos.x, oldPos.y + oldZ, oldPos.z);
        }
      
        if (planeAnchorFound && imageAnchorFound && !itemInit) {
          itemInit = true;
          handleBuildItem();
        }
        /*else if (boxNode != null) {*/ /*
          final oldPos = boxNode.position.value;
          boxNode.position.value =
              vector.Vector3(oldPos.x, oldPos.y + 0.01, oldPos.z);*/ /*
        }*/
        setState(() {});
      });
    });
    // print("end get");
    //sleep(Duration(milliseconds: 1000));
  }

  /* void handlePTPCmd() {
    connectTool.ptpCmd().then((str) {
      print(str);
    });
  }*/

  FutureOr handleOnDeviceDisConnect(bool value) {
    if (value) {
      connectText = "connect";
      isConnect = false;
      setState(() {});
    }
  }

  void handleBuildItem() {
    print("draw box item");

    boxItem = ARKitBox(materials: [
      ARKitMaterial(
        transparency: 1,
        diffuse: ARKitMaterialProperty(color: Colors.indigo),
      )
    ], width: 0.03, height: 0.03, length: 0.03, chamferRadius: 0);
    boxNode = ARKitNode(
      geometry: boxItem,
      position: vector.Vector3(imagePos.x, planePos.y+0.015, imagePos.z-0.015),
    );
    arkitController.add(boxNode);


  }

  void handleDemoBTNPress() {
    connectTool.ptpCmd(200, 70, -90,0)
        .then(handleCmdPTP);
  }

  FutureOr handleCmdPTP(String value) {
    print("cmd ptp:" + value);
  }

  void handlePickUpPress() {
    if (!doPickUp) {
      doPickUp = true;
    }
    connectTool.ptpCmd(200, 70, -5,0);
  }
}
