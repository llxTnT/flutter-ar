import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';

class MyGesture extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return _MyGesture();
  }
}

class _MyGesture extends State<MyGesture> {
  String _eTxt = "No Gesture";
  double _left = 20.0;
  double _top = 20.0;

  bool _toogle = false; //变色开关

  TapGestureRecognizer _tp = new TapGestureRecognizer();  //初始化tab的GestureRecognizer

  void updateText(str) {
    setState(() {
      _eTxt = str;
    });
  }

  @override
  void dispose() {
    //用到GestureRecognizer的话一定要调用其dispose方法释放资源
    _tp.dispose();
    super.dispose();
  }

  // 点击、双击、长按
  Widget _getGes() {
    return GestureDetector(
      child: Container(
        width: 150.0,
        height: 150.0,
        color: Colors.black12,
        alignment: Alignment.center,
        child: Text(
          _eTxt,
          style: TextStyle(color: Colors.white),
        ),
      ),
      onTap: () => updateText("tab"), //单击
      onDoubleTap: () => updateText("double tab"), //双击
      onLongPress: () => updateText("long press"), //长按
    );
  }

  //拖动、滑动
  Widget _drag() {
    return ConstrainedBox(
      constraints: BoxConstraints.tightFor(width: 200.0, height: 200.0),
      child: Stack(
        children: <Widget>[
          Container(
            width: 200.0,
            height: 200.0,
            color: Colors.black12,
          ),
          Positioned(
            left: _left,
            top: _top,
            child: GestureDetector(
              child: CircleAvatar(
                radius: 20.0,
                child: Text("拖动"),
              ),
              onPanDown: (e) => print("按下的位置${e.globalPosition}"), //手指按下时会触发此回调
              onPanUpdate: (e) { //手指滑动时会触发此回调
                //用户手指滑动时，更新偏移，重新构建
                setState(() {
                  _left += e.delta.dx;
                  _top += e.delta.dy;
                });
              },
              //手指滑动结束
              onPanEnd: (e) {
                print(e.velocity);
              },
            ),
          ),
        ],
      ),
    );
  }

  // GestureRecognizer
  Widget _getRichText() {
    return Text.rich(TextSpan(children: [
      TextSpan(text: "这是Txt1"),
      TextSpan(
          text: "手势操作",
          style: TextStyle(
              fontSize: 30.0, color: _toogle ? Colors.red : Colors.tealAccent),
          // tab事件
          recognizer: _tp
            ..onTap = () {
              setState(() {
                _toogle = !_toogle;
              });
            })
    ]));
  }

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return Scaffold(
      appBar: AppBar(
        title: Text("gesture"),
      ),
      body: Column(
        children: <Widget>[_drag(),_getRichText()],
      ),
    );
  }
}