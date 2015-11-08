// Copyright (c) 2015, <your name>. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

import 'dart:html' as html;
import 'package:stagexl/stagexl.dart' as sxl;

void main() {
  var resourceManager = new sxl.ResourceManager()
    ..addTextureAtlas('CLAW', 'spritesheets/CLAW.json', sxl.TextureAtlasFormat.JSON);

  sxl.RenderLoop renderLoop = new sxl.RenderLoop();
  var so = new sxl.StageOptions();
  so.renderEngine = sxl.RenderEngine.WebGL;
  sxl.Stage stage = new sxl.Stage(html.querySelector('#stage'), options: so);
  renderLoop.addStage(stage);

  resourceManager.load().then((resourceManager) {
    var textureAtlas = resourceManager.getTextureAtlas('CLAW');
    var bd = textureAtlas.getBitmapData('REZ/CLAW/CLAW/IMAGES/FRAME031');
    var b0 = new sxl.Bitmap(bd);
    stage
      ..addChild(b0);
  });
}
