import 'dart:async';
import 'dart:convert';
import 'dart:io';

import 'package:FlutterARKit/tool/dobot_magician_b_tool.dart';
import 'package:arkit_plugin/arkit_plugin.dart';
import 'package:flutter/material.dart';
import 'package:vector_math/vector_math_64.dart' as vector;

class FigerHitePage extends StatefulWidget {
  @override
  FigerHitePageState createState() => FigerHitePageState();
}

class FigerHitePageState extends State<FigerHitePage> {
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
  vector.Vector3 magicianARPos;
  vector.Vector3 magicianDevicePos;
  List<ARKitNode> tapNodeList;

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

  ARKitNode movingNode;
  bool busy = false;
  Scaffold scaffold;
  GlobalKey _key = GlobalKey();

  @override
  void dispose() {
    arkitController?.dispose();
    timer?.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    if (tapNodeList == null) {
      tapNodeList = List();
    }
    return Scaffold(
      appBar: AppBar(
        title: const Text('Magician finger click Demo'),
        backgroundColor: Colors.indigo,
      ),
      body: Stack(
        children: <Widget>[
          Container(
              child: GestureDetector(
                  key: _key,
                  child: Container(
                    child: containerView = ARKitSceneView(
                      showFeaturePoints: false,
                      showWorldOrigin: true,
                      onARKitViewCreated: onARKitViewCreated,
                      detectionImagesGroupName: "AR Resources",
                    ),
                  ),
                  onTapDown: handleTapDown)),
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
                child: Text("through item"), onPressed: handleTroughBtnPress),
          ),
          /* Positioned(
            right: 50,
            width: 200,
            top: 80,
            child: RaisedButton(
                child: Text("Pick up"), onPressed: handlePickUpPress),
          ),*/

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
    arkitController.add(
        ARKitNode(
          geometry: ARKitLine(
            fromVector: vector.Vector3(0, 0, 0),
            toVector: vector.Vector3(0.3, 0, 0),
            materials: [
              ARKitMaterial(
                transparency: 1,
                diffuse: ARKitMaterialProperty(color: Colors.green),
              )
            ],
          ),
          position: vector.Vector3(0, 0, 0),
        ),
        parentNodeName: node.name);

    arkitController.add(
        ARKitNode(
          geometry: ARKitLine(
            fromVector: vector.Vector3(0, 0, 0),
            toVector: vector.Vector3(0.0, 0.5, 0),
            materials: [
              ARKitMaterial(
                transparency: 1,
                diffuse: ARKitMaterialProperty(color: Colors.green),
              )
            ],
          ),
          position: vector.Vector3(0, 0, 0),
        ),
        parentNodeName: node.name);

    arkitController.add(
        ARKitNode(
          geometry: ARKitLine(
            fromVector: vector.Vector3(0, 0, 0),
            toVector: vector.Vector3(0.0, 0, 0.7),
            materials: [
              ARKitMaterial(
                transparency: 1,
                diffuse: ARKitMaterialProperty(color: Colors.green),
              )
            ],
          ),
          position: vector.Vector3(0, 0, 0),
        ),
        parentNodeName: node.name);

    arkitController.getNodeWorldPosition(imageAnchor.nodeName).then((e1) {
      if (e1 != null) {
        magicianARPos = e1;
        imageAnchorFound = true;
        print("image node x:" +
            e1.x.toStringAsFixed(5) +
            "  y:" +
            e1.y.toStringAsFixed(5) +
            "   z:" +
            e1.z.toStringAsFixed(5));
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
      connectTool.ptpCmd(0, -200, 0, 0);
      magicianDevicePos = vector.Vector3(0, -200, 0);
      setState(() {
        startGetPoseFuture();
      });
    }
  }

  void startGetPoseFuture() {
    timer = Timer.periodic(const Duration(milliseconds: 1000), (timer) {
      connectTool.getPose().then((str) {
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
          oldZ = oldZ / 1000;
          final oldPos = boxNode.position.value;
          // print("move y:"+oldZ.toStringAsFixed(5));
          boxNode.position.value =
              vector.Vector3(oldPos.x, oldPos.y + oldZ, oldPos.z);
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

  void handleTroughBtnPress() {
    tapNodeList.forEach((node) {
      var distanceX = (magicianARPos.x - node.position.value.x) * 1000;
      var distancey = (magicianARPos.y - node.position.value.y) * 1000;
      var distancez = (magicianARPos.z - node.position.value.z) * 1000;
      print("box to magician x:" +
          distanceX.toStringAsFixed(4) +
          "  y:" +
          distancey.toStringAsFixed(4) +
          "  z:" +
          distancez.toStringAsFixed(4));
      connectTool.ptpCmd(-distancez+25, -200-distanceX+25,-distancey+40,0).then(handleCmdPTP);
    });
    // connectTool.ptpCmd(200, 70, -90,0)
  }

  FutureOr handleCmdPTP(String value) {
    print("cmd ptp:" + value);
  }

  void handleTapDown(TapDownDetails details) {
    print("on tap down global pos x:" +
        details.globalPosition.dx.toStringAsFixed(4) +
        "  y:" +
        details.globalPosition.dy.toStringAsFixed(4));
    print("on tap down local pos x:" +
        details.localPosition.dx.toStringAsFixed(4) +
        "  y:" +
        details.localPosition.dy.toStringAsFixed(4));

    final RenderBox box = _key.currentContext.findRenderObject();
    final size = box.size;
    // final topLeftPosition = box.localToGlobal(Offset.zero);
    print("total size width:" +
        size.width.toStringAsFixed(4) +
        "  height:" +
        size.height.toStringAsFixed(4));

    this
        .arkitController
        .performHitTest(
            x: details.localPosition.dx / size.width,
            y: details.localPosition.dy / size.height)
        .then((results) {
      print("hit test result:" + results.isNotEmpty.toString());
      if (results.isNotEmpty) {
        final ARKitMaterial material = ARKitMaterial(
          diffuse: ARKitMaterialProperty(color: Colors.white),
        );

        ARKitSphere sphere = ARKitSphere(
          materials: [material],
          radius: 0.01,
        );
        final point = results.firstWhere(
            (o) => o.type == ARKitHitTestResultType.featurePoint,
            orElse: () => null);
        if (point == null) {
          return;
        }
        //  print("point "+point.toString());

        final position = vector.Vector3(
          point.worldTransform.getColumn(3).x,
          point.worldTransform.getColumn(3).y,
          point.worldTransform.getColumn(3).z,
        );

        print("point world pos  x: " +
            position.x.toStringAsFixed(4) +
            "  y:" +
            position.y.toStringAsFixed(4) +
            "  z:" +
            position.z.toStringAsFixed(4));

        boxItem = ARKitBox(materials: [
          ARKitMaterial(
            transparency: 1,
            diffuse: ARKitMaterialProperty(color: Colors.indigo),
          )
        ], width: 0.03, height: 0.03, length: 0.03, chamferRadius: 0);
        ARKitNode newNode = ARKitNode(
          geometry: boxItem,
          position: position,
        );
        tapNodeList.add(newNode);
        this.arkitController.add(newNode);
      }
    });
  }
}
