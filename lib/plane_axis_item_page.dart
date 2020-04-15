import 'package:arkit_plugin/arkit_plugin.dart';
import 'package:flutter/material.dart';
import 'package:vector_math/vector_math_64.dart' as vector;

class PlaneBuildAxisPage extends StatefulWidget {
  @override
  _PlaneBuildAxisPageState createState() => _PlaneBuildAxisPageState();
}

class _PlaneBuildAxisPageState extends State<PlaneBuildAxisPage> {
  ARKitController arkitController;
  bool anchorWasFound = false;
  ARKitSceneView containerView;

  ARKitPlane plane;
  ARKitNode sourceNode;
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
          child: containerView=ARKitSceneView(
            showFeaturePoints: false,
            planeDetection: ARPlaneDetection.horizontal,
            detectionImagesGroupName: "AR Resources",
            onARKitViewCreated: onARKitViewCreated,
          ),
        ),
      );

  void onARKitViewCreated(ARKitController arkitController) {
    this.arkitController = arkitController;
    this.arkitController.onAddNodeForAnchor = _handleAddAnchor;
    this.arkitController.add(sourceNode= ARKitNode(
    geometry: ARKitSphere(
      materials: [ ARKitMaterial(
        transparency: 1,
        diffuse: ARKitMaterialProperty(color: Colors.white),
      )
     /* ARKitMaterial(
      lightingModelName: ARKitLightingModel.lambert,
      diffuse: ARKitMaterialProperty(image: 'earth.jpg'),
    )*/
      ],
      radius: 0.01,
    ),
    position: vector.Vector3(0, 0, 0),
    //rotation: vector.Vector4(1, 0, 0, -math.pi / 2),
    ));
    this.arkitController.add(ARKitNode(
        geometry: ARKitLine(fromVector: vector.Vector3(
            0,0,0),
            toVector: vector.Vector3(
                0.1,0,0))
    ));
    this.arkitController.add(ARKitNode(
      geometry: ARKitLine(fromVector: vector.Vector3(
          0,0,0),
          toVector: vector.Vector3(
              0,0.1,0))
    ));
    this.arkitController.add(ARKitNode(
        geometry: ARKitLine(fromVector: vector.Vector3(
            0,0,0),
            toVector: vector.Vector3(
                0,0,0.1))
    ));
    print("on arkit create");
    print(containerView.detectionImagesGroupName);

  }

  void _handleAddAnchor(ARKitAnchor anchor) {
    if (anchor is ARKitImageAnchor) {
      _addImageAchor(arkitController, anchor);
    }else if (anchor is ARKitPlaneAnchor)
      {
        if(!anchorWasFound)
        _addPlane(arkitController, anchor);
      }
  /*  if (!(anchor is ARKitPlaneAnchor)) {
      return;
    }*/
   //
  }

  void _addPlane(ARKitController controller, ARKitPlaneAnchor anchor) {
    anchorWasFound=true;
    print("plane anchor:"+
        " x:"+anchor.center.x.toStringAsFixed(3)+
        " y:"+anchor.center.x.toStringAsFixed(3)+
        " z:"+anchor.center.x.toStringAsFixed(3)
    );
    anchorId = anchor.identifier;
    final sphere = ARKitSphere(
      materials: [ ARKitMaterial(
        transparency: 1,
        diffuse: ARKitMaterialProperty(color: Colors.red),
      )],
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
      fromVector: vector.Vector3(
          0,0,0),
      toVector: vector.Vector3(
           0.3, 0, 0),
      materials: [
        ARKitMaterial(
          transparency: 1,
          diffuse: ARKitMaterialProperty(color: Colors.green),
        )
      ],
    );

    yAxis = ARKitLine(
      fromVector: vector.Vector3(
          0,0,0),
      toVector: vector.Vector3(
          0,0.3,0),
      materials: [
        ARKitMaterial(
          transparency: 1,
          diffuse: ARKitMaterialProperty(color: Colors.yellow),
        )
      ],
    );

    zAxis = ARKitLine(
      fromVector: vector.Vector3(
        0,0,0),
      toVector: vector.Vector3(
          0,0,0.3),
      materials: [
        ARKitMaterial(
          transparency: 1,
          diffuse: ARKitMaterialProperty(color: Colors.lightBlue),
        )
      ],
    );
    node = ARKitNode(
      geometry: sphere,
      position: vector.Vector3(0,0,0),
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
    controller.add(node,parentNodeName:anchor.nodeName);
    controller.add(xNode,parentNodeName:anchor.nodeName);
    controller.add(yNode,parentNodeName:anchor.nodeName);
    controller.add(zNode,parentNodeName:anchor.nodeName);
  }

  void _addImageAchor(ARKitController arkitController, ARKitImageAnchor anchor) {
    anchorId = anchor.identifier;
    final sphere = ARKitSphere(
      materials: [ ARKitMaterial(
        transparency: 1,
        diffuse: ARKitMaterialProperty(color: Colors.red),
      )],
      radius: 0.01,
    );
    anchor.referenceImageName;
    final imagePosition = anchor.transform.getColumn(3);
    final node = ARKitNode(
      geometry: sphere,
      position:
      vector.Vector3(imagePosition.x, imagePosition.y, imagePosition.z),
      eulerAngles: vector.Vector3.zero(),
    );
    arkitController.add(node);
  }
}
