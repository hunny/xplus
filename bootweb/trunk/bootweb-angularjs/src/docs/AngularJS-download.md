# Download File

## Create and Download pdf from server using spring java and angular

Create Temporay file and then delete that file doesnot work.
So, We need to be carefull about FileInputStream and FileOutputStreams.
They must be closed before delete the file.
Here is complete example of how to create pdf and download pdf in two step:

```java
@RequestMapping(value = "createPdf" , method=RequestMethod.POST)
    public @ResponseBody JSONResult createPdf(){
      JSONResult result = new JSONResult();
      File f = null;
      FileOutputStream out = null;
      try{
            f = File. createTempFile( "tmpPdf", ".pdf" , new File("D:\\temp" ));
            out = new FileOutputStream(f);
            IOUtils. copy( new FileInputStream( "E:\\fs.pdf" ), out );
            out.flush( );
            out.close( );
            result.setData( f.getAbsolutePath( ) );
            result.setSuccess( true );
          } catch ( Exception e ) {
                  e.printStackTrace( );
                  result.setSuccess( false );
                   if(null != out){
                         try {
                              out.close( );
                        } catch ( IOException e1 ) {
                              e1.printStackTrace();
                        }
                  }
                   if(null != f){
                        f.delete( );
                  }
            } finally {
                   if(null != out){
                         try {
                              out.close( );
                        } catch ( IOException e1 ) {
                              e1.printStackTrace();
                        }
                  }
            }
      return result;
    }
   
    @RequestMapping(value = "getPDF", method=RequestMethod.GET)
    public void getPdf(@QueryParam ("filePath" ) String filePath, HttpServletResponse response) throws IOException{
      File f = new File(filePath);
      if(f.exists( )){
            FileInputStream inputStream = new FileInputStream( filePath);
            IOUtils. copy( inputStream, response.getOutputStream( ) );
            response.setContentType( "application/pdf" );
            response.setHeader( "Content-Disposition", "attachment; filename=somefile.pdf");
            response.flushBuffer( );
            inputStream.close( );
            f.delete( );
      } else {
             throw new IOException();
      }
    }
```

```javascript
$scope.downloadPdf = function(){
      var win = window.open( '', '_blank');
      try {
            $http.post(getContext() + '/documents/createPdf').success(function (data, status, headers, config){
                  win.location.href = getContext() + '/documents/getPDF?filePath=' +encodeURIComponent(data.data);
                  win.focus();
                   });
      } catch(e) {
            win.close();
             return false ;
      }
};
```

## Best way to download File From Server using java spring and angular js

I think the best way is to make an AJAX request which generates a file on the server. On the browser, when that request completes, use window.open() to download the generated file.

```
<a target="_self" href="example.com/uploads/asd4a4d5a.pdf" download="foo.pdf">
```
Direct File Download and show in new Tab

```java
@RequestMapping (value = "exportPdf/{id}", method=RequestMethod. GET)
    public void exportPdf( @PathVariable ("id" ) Long id, HttpServletResponse response) throws IOException{      
      IOUtils.copy( new FileInputStream( "E:\\SDS-ALL\\nobility-ws\\com.fs\\OASIS_ADMISSION.pdf" ), response.getOutputStream( ) );
      response.setContentType( "application/pdf" );
      response.setHeader( "Content-Disposition", "attachment; filename=somefile.pdf");
      response.flushBuffer( );
    }
```
Open that downloaded file in new Pdf:

```
< a href ="http://localhost:8080/blog//documents/exportPdf/2" target ="_blank"> Click </a>
```

Large size file also work in this way:

Download Base64 value using Ajax and Show as Data URI

```java
@RequestMapping(value = "exportPdfInPost/{id}" , method=RequestMethod.POST )
    public @ResponseBody JSONResult exportPdf2( @PathVariable ("id" ) Long id, Document document, HttpServletResponse response) throws IOException{
      System. out.println("Document Pojo:" + document);      
      String pdfBase64 = Base64. encodeBase64String( IOUtils.toByteArray( new FileInputStream( "E:\\SDS-ALL\\nobility-ws\\com.fs\\OASIS_ADMISSION.pdf" )) );
      JSONResult result = new JSONResult();
      result.setData( "data:application/pdf;base64," +pdfBase64);
      return result;
    }
```

clien:

```
< button class ="btn pull-right" style ="margin-top: 4px" data-ng-click ="downloadPdf();"><i class= "fa fa-file-pdf-o" ></i> Download PDF</ button>&nbsp;
$scope.downloadPdf = function(){
      var win = window.open( '', '_blank');
      try {
              $http.post(getContext() + '/documents/exportPdfInPost/2', {id:'2'}).success(function           (data, status, headers, config){
                    win.location.href = data.data;
              });
      } catch(e) {
            win.close();
             return false ;
      }          
};
```

Use it for less size file: If file size is too heavy browser will hang as data will be loaded in the browser memory. Still firefox work quite well with heavy size data.
Chrome, safari hang. IE and Opera have no plugin embedded and they cant view even small file.
Safari auto open in new child window and show in Adobe pdf as embedded default.

```
// If you want to open value in new Tab:
$scope.open = function(docDefinition) {
      var win = window.open( '', '_blank');
      try {
              win.location.href = docDefinition;
      } catch(e) {
            win.close();
             return false ;
      }
    };
```
Download File Client Side Only:

```
$scope.openPdf = function(){
      var docDefinition =data:application/pdf;base64,JVBERi0xLjMKJf////8KOCAwago8lJUVPRgo=" ; 
      var link = document.createElement( "a");
            link.setAttribute( "href", docDefinition);
            link.setAttribute( "download", "flowsheet.pdf" );
            document.body.appendChild(link)
            link.click();
      
    };
```
Download file using Ajax:

```
var data=data:application/pdf;base64,JVBERi0xLjMKJf////8KOCAwago8lJUVPRgo=" ;
$http.get('http://localhost:8080/blog//documents/exportPdf/2').success(function (data, status, headers, config){            
             var link = document.createElement("a" );
            link.setAttribute( "href", data.data);
            link.setAttribute( "download", "flowsheet.pdf" );
            document.body.appendChild(link)
            link.click();
      });
```

***********************************************************************
Download Any content:
You can do something like this using Blob.
```
<a download="content.txt" ng-href="{{ url }}">download</a>
```
in your controller:
```
var content = 'file content';var blob = new Blob([ content ], { type : 'text/plain' });
$scope.url = (window.URL || window.webkitURL).createObjectURL( blob );
```
=======================================================================

New Idea:
Better show large size file in new tab without Ajax.
Two step. 1 request to generate file in server and another request to download it.
Lets Do It.

Client:

```javascript
$scope.downloadPdf = function(){
      var win = window.open( '', '_blank');
      try {
            $http.post(getContext() + '/documents/exportPdfInPost/2', {id:'2'}).success(function (data, status, headers, config){
                  win.location.href = 'http://localhost:8080/blog//documents/exportPdf/2' ;
                  win.focus();
                   });
      } catch(e) {
            win.close();
             return false ;
      }
};
```
First create pdf in the server. and second download it using seperate url.

Server:

```java
@RequestMapping (value = "exportPdf/{id}", method=RequestMethod. GET )
    public void exportPdf( @PathVariable ("id" ) Long id, HttpServletResponse response) throwsIOException{      
      IOUtils. copy( new FileInputStream( "E:\\SDS-ALL\\nobility-ws\\com.fs\\OASIS_ADMISSION.pdf" ), response.getOutputStream( ) );
      response.setContentType( "application/pdf" );
      response.setHeader( "Content-Disposition" , "attachment; filename=somefile.pdf");
      response.flushBuffer( );
    }
```
And

=================================================
Download using get In One Short:
In the get Request: send all the parameters: and then just do this:

```javascript
var win = window.open('', '_blank');
try {
     win.location.href = 'http://localhost:8080/blog//documents/exportPdf/2 ?encodeURIComponent(selected_user.Username) encode params...' ;
     win.focus();
} catch(e) {
    win.close();
    return false ;
}
```
=================================




