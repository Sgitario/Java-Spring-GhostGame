function AppViewModel() {
    this.string = ko.observable("");
    this.addedLetter = ko.observable("");
    this.finished = ko.observable("");
    this.winner = ko.observable("");
    this.token = "";
    this.stringInput = null;
    
    this.addedLetter.subscribe(function(newValue) {
    	if (newValue && newValue != "") {
    		var letter = newValue;
    		
    		viewModel.string(viewModel.string() + letter);
        	
        	var url = CONTEXT_ROOT + "/ghost/addLetter";
        	
        	var request = { token: viewModel.token, letter: letter };
        	
        	$.ajax({url: url,
        		type: "POST",
        		data: JSON.stringify(request),
        		contentType: "application/json",
        		dataType: "json",
        		success: function(response) {
        			if (viewModel.error) {
        				alert(viewModel.error);
        			} else {
        				if (response.letter != null) {
        					viewModel.string(viewModel.string() + response.letter);
        				}
        				
        				if (response.finished == true) {
        					viewModel.winner(response.winner);
        					
        					viewModel.resetGame();
        				} 
        				
        				viewModel.addedLetter("");
        			}
        		}});
    	}
    });
    
    /**
     * Start game call.
     */
    this.startGame = function() {
    	viewModel.string("");
    	viewModel.finished(true);
    	viewModel.winner("");
    	
    	var url = CONTEXT_ROOT + "/ghost/startGame";
    	
    	var request = { lang: "eng", level: "1" };
    	
    	$.ajax({url: url,
    		type: "POST",
    		data: JSON.stringify(request),
    		contentType: "application/json",
    		dataType: "json",
    		success: function(response) {
    			if (response.error) {
    				alert(response.error);
    			} else {    				
    				viewModel.token = response.token;
    				
    				viewModel.finished(false);
    			}
    		}});
    };
    
    /**
     * End game call.
     */
    this.endGame = function() {
    	var url = CONTEXT_ROOT + "/ghost/endGame";
    	
    	var request = { token: viewModel.token };
    	
    	$.ajax({url: url,
    		type: "POST",
    		data: JSON.stringify(request),
    		contentType: "application/json",
    		dataType: "json",
    		success: function(response) {
    			if (viewModel.error) {
    				alert(viewModel.error);
    			} else {    				
    				viewModel.finished(true);
    				
    				// Restart game    				
    				viewModel.startGame();
    			}
    		}});
    };
    
    /**
     * Reset game
     */
    this.resetGame = function() {
    	viewModel.finished(true);
    	viewModel.token = null;
    };
    
    // Initialize
    this.init = function(entities) {
    	for (var index = 0; index < entities.length; index++) {
    		var entity = entities[index];
    		 
    		entity.init(entity, this);
    	}
    	
    	viewModel.resetGame();
    };
}

var viewModel = new AppViewModel();

$(document).ready(function() {
	
	// Start button entity
	var entities = new Array();
	entities.push(new StartEntity($("#btnStartGame")));
	entities.push(new EndEntity($("#btnEndGame")));
	entities.push(new BoxInputLetterEntity($("#inputLetter")));
	entities.push(new LettersContainerEntity($("#letters")));
	entities.push(new FantasmicoEntity($("#imgFantasmico")));
	entities.push(new MessageEntity($("#userWinsMessage"), "user"));
	entities.push(new MessageEntity($("#computerWinsMessage"), "computer"));
	
	viewModel.init(entities);
	
	//Activates knockout.js
	ko.applyBindings(viewModel);
	
	
});