package com.betterDocs.crawlerTests

import java.io.File

import com.betterdocs.crawler.ZipBasicParser
import org.apache.commons.compress.archivers.zip._
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, BeforeAndAfterEach, FlatSpec}

import scala.collection.mutable


/**
 * Created by sivaramt on 03-03-2015.
 */
class ZipBasicParserTest extends FlatSpec with BeforeAndAfterAll with BeforeAndAfter with BeforeAndAfterEach{



  val zipParser= ZipBasicParser
  val zipFilesLocation=System.getProperty("user.dir")+"/src/test/resources/zipFiles"
  var twoZipJavaClassesLoc = new ZipFile(new File(zipFilesLocation+"/twoZipJavaClasses.zip"))
  var mixedZipFile=new ZipFile(new File(zipFilesLocation+"/mixedFiles.zip"))
  val classBContent:String ="public class b {\n\tInteger b=1;\n}"
  val classAContent:String ="public class A {\n\tInteger a =0;\n}"
  val classContents:List[(String)]=List(classBContent,classAContent)
  val classNames :List[(String)] = List("ClassB","utilities/ClassA.java")
  val packageNames:List[(String)] = List("utilities")
  var classDetails = mutable.ArrayBuffer[(String, String)]()
  var allPackages:List[(String)] = List(zipFilesLocation+"JavaFileZipped.zip",zipFilesLocation+"twoZipJavaClasses.zip")
  var allFiles=Array[(File)]()


  override def beforeAll(): Unit = {
    this.classDetails = zipParser.readFilesAndPackages(twoZipJavaClassesLoc)._1
    this.allPackages = zipParser.readFilesAndPackages(twoZipJavaClassesLoc)._2
    this.allFiles = zipParser.listAllFiles(zipFilesLocation)
  }


  markup("------------Unit Test cases for method ZipBasicParser.readFilesAndPackages()---------------")
  behavior of "Zip Parser"
  it should "parse java file names from the zip file" in {
    classDetails.foreach(z=>assert(classNames.contains(z._1)))
  }
  it should "parse the java file contents from the zip File" in {
    classDetails.foreach(z=>assert(classContents.contains(z._2)))
  }
  it should "parse all packages from the zip File" in {
    if (!allPackages.isEmpty) assert(false)
    else {
      allPackages.foreach(z => assert(packageNames.contains(z)))
    }
  }
  it should "not parse the non java files" in {
    this.classDetails = zipParser.readFilesAndPackages(mixedZipFile)._1
    classDetails.foreach(z=>assert(classNames.contains(z._1)))
  }


  markup("------------Unit Test cases for method ZipBasicParser.listAllFiles()---------------")
  behavior of "List Files"
  it should "list all the files in the directory" in {
    allPackages.foreach(z=>info("The actual file location is :"+z))
    if(!allFiles.isEmpty) assert(false) else {
      allFiles.foreach(z=>assert(allPackages.contains(z)))
    }
  }



}
