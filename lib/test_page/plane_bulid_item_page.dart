import 'dart:math' as math;
import 'package:arkit_plugin/arkit_plugin.dart';
import 'package:flutter/material.dart';
import 'package:vector_math/vector_math_64.dart' as vector;

class PlaneBuildItemPage extends StatefulWidget {
  @override
  _PlaneBuildItemPageState createState() => _PlaneBuildItemPageState();
}

class _PlaneBuildItemPageState extends State<PlaneBuildItemPage> {
  ARKitSceneView mainScene;
  ARKitController arkitController;
  ARKitPlane plane;
  ARKitNode node;

  ARKitMaterial itemMaterial;
  ARKitNode itemNode;

  //ARKitSphere itemSphere;
  ARKitBox itemBox;

  String anchorId;

  @override
  void dispose() {
    arkitController?.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context){


    Scaffold scaffold= Scaffold(
        appBar: AppBar(title: const Text('Plane Detection')),
        body: Container(
          child: mainScene=ARKitSceneView(
            showFeaturePoints: false,
            planeDetection: ARPlaneDetection.horizontal,
            onARKitViewCreated: onARKitViewCreated,
            enablePanRecognizer: true,
            enablePinchRecognizer: true,
            enableTapRecognizer: true,
          ),
        ),
      );

    return scaffold;
  }

  void onARKitViewCreated(ARKitController arkitController) {
    this.arkitController = arkitController;
    this.arkitController.onAddNodeForAnchor = _handleAddAnchor;
    this.arkitController.onUpdateNodeForAnchor = _handleUpdateAnchor;
    this.arkitController.onNodePinch = _handleNodePinch;
    this.arkitController.onNodePan=_handleNodePan;
    print("on view create");
  }

  void _handleAddAnchor(ARKitAnchor anchor) {
    if (!(anchor is ARKitPlaneAnchor)) {
      return;
    }
    _addPlane(arkitController, anchor);
    _addEarth(arkitController);
  }

  void _handleUpdateAnchor(ARKitAnchor anchor) {
    if (anchor.identifier != anchorId) {
      return;
    }
    final ARKitPlaneAnchor planeAnchor = anchor;
    node.position.value =
        vector.Vector3(planeAnchor.center.x, 0, planeAnchor.center.z);
    plane.width.value = planeAnchor.extent.x;
    plane.height.value = planeAnchor.extent.z;
  }

  void _addPlane(ARKitController controller, ARKitPlaneAnchor anchor) {
    anchorId = anchor.identifier;
    plane = ARKitPlane(
      width: anchor.extent.x,
      height: anchor.extent.z,
      materials: [
        ARKitMaterial(
          transparency: 0.5,
          diffuse: ARKitMaterialProperty(color: Colors.red),
        )
      ],
    );

    node = ARKitNode(
      geometry: plane,
      position: vector.Vector3(anchor.center.x, 0, anchor.center.z),
      rotation: vector.Vector4(1, 0, 0, -math.pi / 2),
    );
    controller.add(node, parentNodeName: anchor.nodeName);
  }

  void _addEarth(ARKitController controller) {
    this.itemMaterial = ARKitMaterial(
      transparency: 1.0,
      diffuse: ARKitMaterialProperty(image: "magician.png"),
    );

    this.itemBox = ARKitBox(
        materials: [this.itemMaterial],
        width: 0.1,
        height: 0.1,
        length: 0.1,
        chamferRadius: 0);

    this.itemNode = ARKitNode(
      geometry: itemBox,
      position: vector.Vector3(0, 0.0, 0.05),
    );
    this.arkitController.add(itemNode, parentNodeName: node.name);

    ARKitNode itemNode2 = ARKitNode(
      geometry: itemBox,
      position: vector.Vector3(0, 0.1, 0.05),
    );
    this.arkitController.add(itemNode2, parentNodeName: node.name);

    ARKitNode itemNode3 = ARKitNode(
      geometry: itemBox,
      position: vector.Vector3(0, -0.1, 0.05),
    );
    this.arkitController.add(itemNode3, parentNodeName: node.name);

    ARKitNode itemNode4 = ARKitNode(
      geometry: itemBox,
      position: vector.Vector3(0.1, 0, 0.05),
    );
    this.arkitController.add(itemNode4, parentNodeName: node.name);

    ARKitNode itemNode5 = ARKitNode(
      geometry: itemBox,
      position: vector.Vector3(-0.1, 0, 0.05),
    );
    this.arkitController.add(itemNode5, parentNodeName: node.name);

    ARKitNode itemNode6 = ARKitNode(
      geometry: itemBox,
      position: vector.Vector3(0, 0, 0.15),
    );
    this.arkitController.add(itemNode6, parentNodeName: node.name);
  }

  void _handleNodePinch(List<ARKitNodePinchResult> pinch) {
    for (var index = 0; index < pinch.length; index++) {
      print("node on pinch1:" + pinch.elementAt(index).nodeName);
      print("node on pinch2:" + pinch.elementAt(index).toString());
    }
  }

  void _handleNodePan(List<ARKitNodePanResult> pans) {
    for (var index = 0; index < pans.length; index++) {
      print("node on pan1:" + pans.elementAt(index).nodeName);
      print("node on pan2:" + pans.elementAt(index).toString());
    }
  }

}
