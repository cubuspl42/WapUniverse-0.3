'use strict';

var _ = require("underscore")
var fs = require("fs")
var gulp = require('gulp')
var path = require("path")
var spritesheet = require('spritesheet-js')

let rezPath = "REZ/CLAW/"
let spriteExtension = ".png"
let outPath = "web/spritesheets/"
let padding = 1

function filterFiles(parentPath, elements, extension) {
  return _.filter(elements, (filename) => {
    var stats = fs.statSync(path.join(parentPath, filename))
    return stats.isFile() && filename[0] != ".";
  })
}

function filterDirs(parentPath, elements) {
  return _.filter(elements, (filename) => {
    let stats = fs.statSync(path.join(parentPath, filename))
    return stats.isDirectory();
  })
}

function crawlSprites(spriteList, folderPath, spriteExtension) {
  let elements = fs.readdirSync(folderPath)
  let files = filterFiles(folderPath, elements)
  let dirs = filterDirs(folderPath, elements)

  for(let i in files) {
    let fileName = files[i]
    let filePath = path.join(folderPath, fileName)
    if(path.extname(filePath) == spriteExtension) {
      spriteList.push(filePath)
    }
  }

  for(let i in dirs) {
    let dirPath = path.join(folderPath, dirs[i])
    crawlSprites(spriteList, dirPath, spriteExtension)
  }
}

function levelSpriteSheetGen(levelDirName) {
  let levelPath = path.join(rezPath, levelDirName)
  let spritePaths = []

  crawlSprites(spritePaths, levelPath, spriteExtension)

  spritesheet(spritePaths, {
    format: 'json',
    path: outPath,
    name: levelDirName,
    fullpath: true,
    padding: padding,
  });
}

function spriteSheetGen() {
  let elements = fs.readdirSync(rezPath)
  let dirs = filterDirs(rezPath, elements)

  for(let i in dirs) {
    let levelDirName = dirs[i]
    levelSpriteSheetGen(levelDirName)
  }
}

gulp.task('spritesheets', function() {
  spriteSheetGen()
});

gulp.task('default', ['spritesheets'])
