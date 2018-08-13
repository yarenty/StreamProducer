package com.yarenty.streamer

/***
  * Config / session - processing input arguments.
  * @TODO: read from properties as alternative!
  *
  * @param args
  *             
  * (C)2018 by yarenty            
  */
class Config(val args: Array[String]) {

  val version = "1.0.0"
  var debugMode = false
  var csv: String = _
  var url: String = _

  
  
  // scalastyle:off 
  // display to stdout
  for (i <- args.indices) {
    val s = args(i)
    s match {
      case "-h" => _help()
      case "-help" => _help()
      case "-v" => _printVersion()
      case "-version" => _printVersion()
      case "-in" => if (args.length > (i + 1)) csv = args(i + 1)
      case "-url" => if (args.length > (i + 1)) url = args(i + 1)
      case "-d" => debugMode = true
      case "-debug" => debugMode = true
      case _ => //skip
    }
  }

  require(args.contains("-in"), "Please specify input data \"-in\"! (-h for help).")
  require(args.contains("-url"), "Please specify URL  \"-url\"! (-h for help).")

  checkIfExist(csv)

  private def checkIfExist(inputFile: String): Boolean = {
    require(new java.io.File(inputFile).exists, s"Could not find input data:'$inputFile' - check if path is correct.")
    true
  }


  private def _help(): Unit = {
    println(
      s"""
         | Stream-Er (version $version).
         | =============================
         | Utility tool that will send stream of data to specific URL from specific source with configured interval.
         | 
         | 
         | Options:
         | -v/-version    - Version information
         | -in <filename> - Input file (CSV)
         | -out <dir>     - Output directory
         | -d/-debug      - Debug mode = detailed logs!
         | -h/-help       - this help
      """.stripMargin
    )
    System.exit(0)
  }

  private def _printVersion(): Unit = {
    println(s"Stream-Er (version $version).")
    System.exit(0)
  }


}
