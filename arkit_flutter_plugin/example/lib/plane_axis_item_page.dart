import 'dart:io';

import 'package:arkit_plugin/arkit_plugin.dart';
import 'package:flutter/material.dart';
import 'package:vector_math/vector_math_64.dart' as vector;
import 'dobot_magician_b_tool.dart';

class PlaneBuildAxisPage extends StatefulWidget {
  @override
  _PlaneBuildAxisPageState createState() => _PlaneBuildAxisPageState();
}

class _PlaneBuildAxisPageState extends State<PlaneBuildAxisPage> {
  ARKitController arkitController;
  bool anchorWasFound = false;
  bool imageAnchorFound = false;
  ARKitSceneView containerView;
  ARKitPlaneAnchor planeAnchor;
  ARKitImageAnchor imageAnchor;
  vector.Vector3 imagePos;
  MagicianConnectTool connectTool;


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

  ARKitSphere planeNodeSphere;




  String anchorId;

  @override
  void dispose() {
    arkitController?.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) => Scaffold(
        appBar: AppBar(title: const Text('Plane Detection')),
        body: Container(
          child: containerView = ARKitSceneView(
            showFeaturePoints: false,
            planeDetection: ARPlaneDetection.horizontal,
            detectionImagesGroupName: "AR Resources",
            onARKitViewCreated: onARKitViewCreated,
            showWorldOrigin: true,
          ),
        ),
      );

  void onARKitViewCreated(ARKitController arkitController) {
    this.arkitController = arkitController;
    this.arkitController.onAddNodeForAnchor = _handleAddAnchor;
    //this.arkitController.onUpdateNodeForAnchor = _handleAnchorChange;

    print(containerView.detectionImagesGroupName);
    connectTool= MagicianConnectTool();
    connectTool.doInitMagicianClient();
    connectTool.doSearchDevice().then((resStr){
      print(resStr);
      connectTool.doConnectDevice(resStr).then((res){
        print("connect res:"+res.toString());
        if(res){
       /*   print("do get pose 1");

          sleep(Duration(milliseconds: 3000));
          print("do get pose 2");

          connectTool.getPose().then((poseStr){
            print("getPose:"+poseStr);
          });*/

        /*  print("do ptp 1");
          sleep(Duration(milliseconds: 5000));
          print("do ptp 2");
          connectTool.ptpCmd().then((ptpStr){
            print("ptpCmd:"+ptpStr);
          });*/
        }
      });
    });
    Future.delayed(Duration(milliseconds: 5000),(){
      print("do getPose1");
      connectTool.getPose().then((poseStr){
        print("getPose1:"+poseStr);
      });
    /*  print("do ptp");
      connectTool.ptpCmd().then((ptpStr){
        print("ptpCmd:"+ptpStr);
      });
      print("do getPose2");
      connectTool.getPose().then((poseStr){
        print("getPose2:"+poseStr);
      });*/
    });
  }

  void _handleAddAnchor(ARKitAnchor anchor) {
    if (anchor is ARKitImageAnchor) {
      if (!imageAnchorFound) _addImageAchor(arkitController, anchor);
    } else if (anchor is ARKitPlaneAnchor) {
      if (!anchorWasFound) _addPlane(arkitController, anchor);
    }
    /*  if (!(anchor is ARKitPlaneAnchor)) {
      return;
    }*/
    //
  }

  void _addPlane(ARKitController controller, ARKitPlaneAnchor anchor) {
    anchorWasFound = true;
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
        planePos=e;
        controller.add(ARKitNode(
          geometry: ARKitLine(
            fromVector: vector.Vector3(0, 0, 0),
            toVector: e,
          ),
        ));
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
    imageAnchorFound = true;
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
      radius: 0.01,
    );
    final imagePosition = anchor.transform.getColumn(3);
    final node = ARKitNode(
      geometry: sphere,
      position:
          vector.Vector3(imagePosition.x, imagePosition.y, imagePosition.z),
      eulerAngles: vector.Vector3.zero(),
    );
    arkitController.add(node);
    if (imageAnchor != null && planeAnchor != null) {
      print("do connect plane and image");
      arkitController.getNodeWorldPosition(imageAnchor.nodeName).then((e1) {
        if (e1 != null) {
          imagePos=e1;
          print("image node x:" +
              e1.x.toStringAsFixed(5) +
              "  y:" +
              e1.y.toStringAsFixed(5) +
              "   z:" +
              e1.z.toStringAsFixed(5));
          print("dis x:"+(imagePos.x-planePos.x).toStringAsFixed(4)+
              "  y:"+(imagePos.y-planePos.y).toStringAsFixed(4)+
              "  z:"+(imagePos.z-planePos.z).toStringAsFixed(4)

          );
              connectNode = ARKitNode(
                geometry: ARKitLine(
                  fromVector: e1,
                  toVector: planePos,
                ),
              );
              arkitController.add(connectNode);
            }
          });

    }
  }


}
