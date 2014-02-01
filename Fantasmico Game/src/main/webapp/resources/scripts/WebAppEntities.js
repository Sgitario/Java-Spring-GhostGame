function StartEntity($element) {
	this.$elem = $element;
	
	// Initial state
	this.$elem.hide();
	this.interval = null;
	
	// Animations
	this.startAnimation = function() {
		if (this.interval == null) {
			var elementToAnimate = this.$elem;
			
			elementToAnimate.show();
			
			this.interval = setInterval(function() {
				elementToAnimate.fadeOut(200, function() {
					elementToAnimate.fadeIn(150, function() {
						elementToAnimate.fadeOut(250, function() {
							elementToAnimate.fadeIn(100);  
						});
					});
				});
			}, 5000);
		}
	};
	
	this.stop = function() {
		if (this.interval != null) {
			window.clearInterval(this.interval);
			
			this.$elem.stop();
			this.$elem.clearQueue();
			this.$elem.hide();
			
			this.interval = null;
		}
	};
	
	this.updatePosition = function($window) { 
		var width = ($window.width() - this.$elem.width()) / 2;
		var height = ($window.height() - this.$elem.height()) / 2;
		
		this.$elem.parent().css({top: height + 'px', left: width + 'px'});
	};
	
	// Initialize
	this.init = function(entity, viewModel) {
		var $window = $(window);
		
		// Align in middle
		$window.resize(function() {			
			entity.updatePosition($window);
		});
		
		entity.updatePosition($window);
		
		viewModel.finished.subscribe(function(newValue) {
			if (newValue == true) {
				entity.startAnimation();
			} else if (newValue == false) {
				entity.stop();
			}
		});
	};
}

function EndEntity($element) {
	this.$elem = $element;
	
	// Initial state
	this.$elem.hide();
	
	this.updatePosition = function($window) { 
		var width = ($window.width() - this.$elem.width()) / 2;
		var height = $window.height() - this.$elem.height() - 100;
		
		this.$elem.parent().css({top: height + 'px', left: width + 'px'});
	};
	
	// Initialize
	this.init = function(entity, viewModel) {
		
		var $window = $(window);
		
		// Align in middle
		$window.resize(function() {			
			entity.updatePosition($window);
		});
		
		entity.updatePosition($window);
		
		viewModel.finished.subscribe(function(newValue) {
			if (newValue == true) {
				entity.$elem.hide();
			} else if (newValue == false) {
				entity.$elem.show();
			}
		});
	};
}

function BoxInputLetterEntity($element) {
	this.$elem = $element;
	
	// Initial state
	this.$elem.hide();
	
	// Animations
	this.startAnimation = function() {
		var elementToAnimate = this.$elem;
		
		elementToAnimate.fadeIn(400, function() {
			elementToAnimate.find("input").focus();
		});
	};
	
	this.stop = function() {
		this.$elem.hide();
	};
	
	// Initialize
	this.init = function(entity, viewModel) {
		
		viewModel.finished.subscribe(function(newValue) {
			if (newValue == false) {
				entity.startAnimation();
			} else if (newValue == true) {
				entity.stop();
			}
		});
		
		viewModel.addedLetter.subscribe(function(newValue) {
			if (viewModel.finished() == false) {
				if (!newValue || newValue == "") {
					entity.$elem.find("input").focus();
				} 
			}
		});
	};
}

function LettersContainerEntity($element) {
	this.$elem = $element;
	
	// Initialize
	this.init = function(entity, viewModel) {
		
		viewModel.finished.subscribe(function(newValue) {
			if (newValue == false) {
				entity.$elem.empty();
			} 
		});
		
		viewModel.string.subscribe(function(newValue) {
			entity.$elem.empty();
			entity.$elem.text(newValue);
		});
	};
}

function FantasmicoEntity($element) {
	this.$elem = $element;
	this.maxWidth = null;
	this.maxHeight = null;
	
	this.updatePosition = function($window) { 
		this.maxWidth = $window.width();
		this.maxHeight = $window.height();
	};
	
	this.startAnimation = function() {	
		
		var maxTime = 5000;
		var elementToAnimate = this;
		
		setInterval(function() {
			elementToAnimate.animate(maxTime);
		}, maxTime);
		
		this.animate(maxTime);
	};
	
	this.animate = function(maxTime) {
		var randomTop = Math.random() * this.maxHeight - this.$elem.height();
		var randomLeft = Math.random() * this.maxWidth - this.$elem.width();
		var randomTime = Math.random() * maxTime;
		var opacity = Math.random() + 0.1;
		
		this.$elem.parent().animate({
			top: randomTop,
			left: randomLeft,
			opacity: opacity
		}, randomTime);
	};
	
	// Initialize
	this.init = function(entity, viewModel) {
		var $window = $(window);
		
		// Align in middle
		$window.resize(function() {			
			entity.updatePosition($window);
		});
		
		entity.maxWidth = $window.width();
		entity.maxHeight = $window.height();
		
		viewModel.finished.subscribe(function(newValue) {
			if (newValue == false) {
				entity.$elem.empty();
			} 
		});
		
		entity.startAnimation();
	};
}

function MessageEntity($element, conditionWinner) {
	this.$elem = $element;
	
	this.$elem.hide();
	
	// Initialize
	this.init = function(entity, viewModel) {
		
		viewModel.winner.subscribe(function(newValue) {
			if (newValue == conditionWinner) {
				entity.$elem.show();
			} else {
				entity.$elem.hide();
			}
		});
	};
}