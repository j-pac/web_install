
//******************************************************************************************
//	My Applications graph update
//******************************************************************************************
$(document).ready(function(){
    $("#repolist").bind('change', function(e){
        $("#my-repolist-form").submit();
    });
    
    $(".hideMyRepoWarning").bind('click', function(e){
        $(".myRepoWarning").slideUp("slow");
        return false;
    });
});

function setupAppsGraph(_url, _repoid, _type, _caller)
{
    if (_caller != null && $(_caller).parent('li') != null){
        $("#appGraphTabs").children().each(function() {
            $(this).removeAttr('id');
        });
		
        $(_caller).parent('li').attr('id', 'selected');
        $(_caller).blur();
        graphtype = _type;
    }
	
    if (_type == 'downloads'){
        $("#pieTableCell").removeClass('statsCellPieAnalytics');
        $("#appschart").show();
        $("#appschart_table").hide();
        _data = "?updateAppsGraph=1&seltime=" + $('#timeline').val() + "&selapp=" + $('#selapps').val() + "&repoid=" + _repoid;
        _url += urlEncode(_data);
        swfobject.embedSWF(flashURL, "appschart", "800", "150", "9.0.0", "expressInstall.html", {"data-file": _url}, {wmode: "opaque"});
        
    } else if (_type == 'androidversion'){
        $("#pieTableCell").addClass('statsCellPieAnalytics');
        $("#appschart").show();
        $("#appschart_table").show();
        _data = "?appPieStatistics=1&seltime=" + $('#timeline').val() + "&selapp=" + $('#selapps').val() + "&repoid=" + _repoid + "&seltype=" + _type;
        _url += urlEncode(_data);
        swfobject.embedSWF(flashURL, "appschart", "400", "200", "9.0.0", "expressInstall.html", {"data-file": _url}, { wmode: "opaque"});
		
        _data = "seltime=" + $('#timeline').val() + "&repoid=" + _repoid + "&selapp=" + $('#selapps').val();
        $.ajax({
            url: appandroidUrl, 
            type: 'GET',
            data: _data,
            success: function(response){
                $("#appschart_table").html(response);
            }
        });
    } else if (_type == 'resolution'){
        $("#pieTableCell").removeClass('statsCellPieAnalytics');
        $("#appschart").hide();
        $("#appschart_table").show();
        $("#appschart_table").html('<br /><br /><br /><img src="' + imagepath + 'repoTableLoader.gif" /><br /><br />');
        _data = "seltime=" + $('#timeline').val() + "&repoid=" + _repoid + "&selapp=" + $('#selapps').val();

        $.ajax({
            url: appResUrl, 
            type: 'GET',
            data: _data,
            success: function(response){
                $("#appschart_table").html(response);
            }
        });
    }
}

//******************************************************************************************
//My Repositories graph update
//******************************************************************************************
function setupReposGraph(_url, _repoid, _type, _caller)
{
    if (_caller != null && $(_caller).parent('li') != null){
        $("#repoGraphTabs").children().each(function() {
            $(this).removeAttr('id');
        });
		
        $(_caller).parent('li').attr('id', 'selected');
        $(_caller).blur();
        graphtype = _type;
    }
	
    if (_type == 'downloads'){
        $("#pieTableCell").removeClass('statsCellPieAnalytics');
        $("#reposchart").show();
        $("#reposchart_table").hide();
        _data = "?updateReposGraph=1&seltime=" + $('#timeline').val() + "&repoid=" + _repoid;
        _url += urlEncode(_data);
        swfobject.embedSWF(flashURL, "reposchart", "800", "150", "9.0.0", "expressInstall.html", {"data-file": _url}, {wmode: "opaque"});
        
    } else if (_type == 'aptoideversion'){
        $("#pieTableCell").addClass('statsCellPieAnalytics');
        $("#reposchart").show();
        $("#reposchart_table").show();
        $("#reposchart_table").html('<br /><br /><br /><img src="' + imagepath + 'repoTableLoader.gif" /><br /><br />');
        _data = "?repoPieStatistics=1&seltime=" + $('#timeline').val() + "&repoid=" + _repoid + "&seltype=" + _type;
        _url += urlEncode(_data);
        swfobject.embedSWF(flashURL, "reposchart", "400", "200", "9.0.0", "expressInstall.html", {"data-file": _url}, {wmode: "opaque"});
		
        _data = "seltime=" + $('#timeline').val() + "&repoid=" + _repoid;
        $.ajax({
            url: repoaptoideUrl, 
            type: 'GET',
            data: _data,
            success: function(response){
                $("#reposchart_table").html(response);
            }
        });
        
    } else if (_type == 'geo'){
        $("#pieTableCell").removeClass('statsCellPieAnalytics');
        $("#reposchart").hide();
        $("#reposchart_table").show();
        $("#reposchart_table").html('<br /><br /><br /><img src="' + imagepath + 'repoTableLoader.gif" /><br /><br />');
        _data = "seltime=" + $('#timeline').val() + "&repoid=" + _repoid;

        $.ajax({
            url: repogeoUrl, 
            type: 'GET',
            data: _data,
            success: function(response){
                $("#reposchart_table").html(response);
            }
        });
        
    } else if (_type == 'cellmodel'){
        $("#pieTableCell").removeClass('statsCellPieAnalytics');
        $("#reposchart").show();
        $("#reposchart_table").hide();
        _data = "?repoBarStatistics=1&seltime=" + $('#timeline').val() + "&repoid=" + _repoid + "&seltype=" + _type;
        _url += urlEncode(_data);
        swfobject.embedSWF(flashURL, "reposchart", "800", "150", "9.0.0", "expressInstall.html", {"data-file": _url}, {wmode: "opaque"});
    }
}


//******************************************************************************************
// Show User Repositories on demand
//******************************************************************************************
function getAJAX_UserRepositories()
{	
    $.ajax({
        url: repoUrl, 
        type: 'GET',
        success: function(response){
            $("#userReposListing").html(response);
            hideAJAXUserRepos();
        }
    });
}


//******************************************************************************************
//OEM graph update
//******************************************************************************************
function setupOEMGraph(_url, _oemid, _type, _caller, _app_id)
{

  if (_caller != null && $(_caller).parent('li') != null){
      $("#repoGraphTabs").children().each(function() {
          $(this).removeAttr('id');
      });
		
      $(_caller).parent('li').attr('id', 'selected');
      $(_caller).blur();
      graphtype = _type;
  }
  
  _chart = ((_app_id == 'appview') ? "appschart" : "reposchart");
  _chart_id = '#'+_chart;
  _chart_table = _chart_id+'_table';
  if (!_app_id)
	  _app_id = 'all';
	
  if (_type == 'downloads'){
      $("#pieTableCell").removeClass('statsCellPieAnalytics');
      $(_chart_id).show();
      $(_chart_table).hide();
      _data = "?updateOEMGraph=1&seltime=" + $('#timeline').val() + "&oemid=" + _oemid + '&selapp=' + _app_id;
      _url += urlEncode(_data);
      swfobject.embedSWF(flashURL, _chart, "800", "150", "9.0.0", "expressInstall.html", {"data-file": _url}, { wmode: "opaque"});
      
  } else if (_type == 'aptoideversion'){
      $("#pieTableCell").addClass('statsCellPieAnalytics');
      $(_chart_id).show();
      $(_chart_table).show();
      $(_chart_table).html('<br /><br /><br /><img src="' + imagepath + 'repoTableLoader.gif" /><br /><br />');
      _data = "?oemPieStatistics=1&seltime=" + $('#timeline').val() + "&oemid=" + _oemid + "&seltype=" + _type;
      _url += urlEncode(_data);
      swfobject.embedSWF(flashURL, _chart, "400", "200", "9.0.0", "expressInstall.html", {"data-file": _url},{wmode: "opaque"});
		
      _data = "seltime=" + $('#timeline').val() + "&oemid=" + _oemid;
      $.ajax({
          url: oemaptoideUrl, 
          type: 'GET',
          data: _data,
          success: function(response){
              $(_chart_table).html(response);
          }
      });
      
  } else if (_type == 'geo'){
      $("#pieTableCell").removeClass('statsCellPieAnalytics');
      $(_chart_id).hide();
      $(_chart_table).show();
      $(_chart_table).html('<br /><br /><br /><img src="' + imagepath + 'repoTableLoader.gif" /><br /><br />');
      _data = "seltime=" + $('#timeline').val() + "&oemid=" + _oemid;

      $.ajax({
          url: oemgeoUrl, 
          type: 'GET',
          data: _data,
          success: function(response){
              $(_chart_table).html(response);
          }
      });
      
  } else if (_type == 'cellmodel'){
      $("#pieTableCell").removeClass('statsCellPieAnalytics');
      $(_chart_id).show();
      $(_chart_table).hide();
      _data = "?oemBarStatistics=1&seltime=" + $('#timeline').val() + "&oemid=" + _oemid + "&seltype=" + _type;
      _url += urlEncode(_data);
      swfobject.embedSWF(flashURL, _chart, "800", "150", "9.0.0", "expressInstall.html",{"data-file": _url},{wmode: "opaque"});
     
  } else if (_type == 'androidversion'){
      $("#pieTableCell").addClass('statsCellPieAnalytics');
      $(_chart_id).show();
      $(_chart_table).show();
      _data = "?oemAppPieStatistics=1&seltime=" + $('#timeline').val() + "&oemid=" + _oemid + "&seltype=" + _type;
      _data += ("&selapp=" + ((_app_id == 'appview') ? $('#selapps').val() : _app_id));
      _url += urlEncode(_data);
      swfobject.embedSWF(flashURL, _chart, "400", "200", "9.0.0", "expressInstall.html", {"data-file": _url}, { wmode: "opaque"});
		
      _data = "seltime=" + $('#timeline').val() + "&oemid=" + _oemid;
      _data += ("&selapp=" + ((_app_id == 'appview') ? $('#selapps').val() : _app_id));
      $.ajax({
          url: oemandroidUrl, 
          type: 'GET',
          data: _data,
          success: function(response){
              $(_chart_table).html(response);
          }
      });
      
	} else if (_type == 'resolution'){
        $("#pieTableCell").removeClass('statsCellPieAnalytics');
        $(_chart_id).hide();
        $(_chart_table).show();
        $(_chart_table).html('<br /><br /><br /><img src="' + imagepath + 'repoTableLoader.gif" /><br /><br />');
        
        _data = "seltime=" + $('#timeline').val() + "&oemid=" + _oemid;
        _data += ("&selapp=" + ((_app_id == 'appview') ? $('#selapps').val() : _app_id));
        $.ajax({
            url: oemresUrl, 
            type: 'GET',
            data: _data,
            success: function(response){
                $(_chart_table).html(response);
            }
        });
    }
}



//******************************************************************************************
//  Misc functions
//******************************************************************************************
function urlEncodeCharacter(c)
{
    return '%' + c.charCodeAt(0).toString(16);
}

function urlDecodeCharacter(str, c)
{
    return String.fromCharCode(parseInt(c, 16));
}

function urlEncode(s)
{
    return encodeURIComponent( s ).replace( /\%20/g, '+' ).replace( /[!'()*~]/g, urlEncodeCharacter );
}

function urlDecode(s)
{
    return decodeURIComponent(s.replace( /\+/g, '%20' )).replace( /\%([0-9a-f]{2})/g, urlDecodeCharacter);
}
