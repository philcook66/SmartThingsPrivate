/**
 *  Brighten My Path
 *
 *  Author: SmartThings
 */
preferences {
	section("When there's movement...") {
		input "motion1", "capability.motionSensor", title: "Where?", multiple: true
	}
	section("Turn on something...") {
		input "switch1", "capability.switch", title: "Switches?", multiple: true
	}
	section("Keep it on when I leave for...") {
		input "offTimeout", "number", title: "Minutes?"
	}
    section("Turn off if away...") {
		input "awayMode", "mode", title: "Away mode", require: false
    }
}

def installed()
{
	subscribe(motion1, "motion.active", motionActiveHandler)
	subscribe(motion1, "motion.inactive", motionInactiveHandler)
    subscribe(location, modeChangeHandler)
}

def updated()
{
	unsubscribe()
	subscribe(motion1, "motion.active", motionActiveHandler)
	subscribe(motion1, "motion.inactive", motionInactiveHandler)
    subscribe(location, modeChangeHandler)
}

def modeChangeHandler(evt)
{
	if (evt.value == awayMode) {
		unschedule(doOff)
    	switch1.off()
    }
}

def motionActiveHandler(evt) {
	unschedule(doOff)
	switch1.on()
}

def motionInactiveHandler(evt) {
	runIn(offTimeout * 60, doOff)
}

def doOff() {
	switch1.off()
}
