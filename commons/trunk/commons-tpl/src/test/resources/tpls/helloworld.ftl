<html>
	<head>
	  <title>${title}</title>
	</head>
	<body>
	  <h1>${title}</h1>
	
	  <p>${exampleObject.id} by ${exampleObject.name}</p>
	
	  <ul>
	    <#list systems as system>
	      <li>${system_index + 1}. ${system.id} from ${system.name}</li>
	    </#list>
	  </ul>
	
	</body>
</html>