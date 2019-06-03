//The Islamic Alarm Clock is a java application developed in KFUPM for educational purpose
//Author: AbdulRahman Manea
//Jan. 4, 2003


//emport statements
import java.util.Date;
import java.util.StringTokenizer;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.applet.*;
import java.net.*;
import java.io.*;


// The main class in the application

public class IslamicAlarmClock extends JFrame
{
	String theCity = "Dhahran";						//A String storing the name of the city, initially Dhahran;
	JLabel time = new JLabel("", JLabel.CENTER);	//A label displaying the time;
	JLabel date = new JLabel("", JLabel.CENTER);	//A label displaying the date;
	JLabel city = new JLabel(theCity, JLabel.CENTER);	//A label displaying the name of the city;
	JMenu file = new JMenu("File");						//The File Menu;	
	JMenu change = new JMenu("Change");					//The Change Menu;
	JMenu help = new JMenu("Help");						//The Help Menu;
	JCheckBoxMenuItem twelveFormat = new JCheckBoxMenuItem("Use 12 Hours Format");//A check box for alternating between 12 Hrs & 24 Hrs formats of the time;
	int format = 24;			//An integer representing the chosen format of the time, initially is 24;
	String amPm = "";			//A String for distinguishing between day time and night time for 12 Hrs format 
	JMenuItem exit = new JMenuItem("Quit");				//A menu item for quitting;
	JMenuItem dispTime = new JMenuItem("Display Time in a New Window");		//A menu item for displaying the time only on a new window;
	JMenuItem changeSound = new JMenuItem("Change Sound File");	//A menu item for changing the sound file;
	JMenuItem changeAlarms = new JMenuItem("Change Alarms");	//A menu item for changing the alarms;
	JMenuItem changeCity = new JMenuItem("Change City");		//A menu item for changing the city;
	JMenuItem instructions = new JMenuItem("Instructions");		//A menu item for instructions;
	JMenuItem about = new JMenuItem("About The Islamic Alarm Clock");	//A menu item for information about this application;
	JCheckBox setAlarmsToPrayers = new JCheckBox("Set Alarms to Prayer Times"); //A check box for setting all the alarms to the prayer times;
	JLabel[] alarmTimes = new JLabel[5];		//An Array of labels displaying the time of alarms in the main window;
	JLabel[] alarmLabels = new JLabel[5];		//An Array of labels displaying the # of each alarm in the main window;
	JLabel[] prayerTimes = new JLabel[5];		//An Array of labels displaying prayer times in the main window;
	JLabel[] prayerLabels = new JLabel[5];		//An Array of labels displaying names of prayers;
	JLabel[] innerAlarmLabels = new JLabel[5];	//An Array of labels displaying # of each alarm in the "Change Alarms" window;
	JLabel[] innerAlarmTimes = new JLabel[5];	//An Array of labels displaying the time of alarms in the "Change Alarms" window;
	static JLabel animation = new JLabel("", JLabel.RIGHT);		//A label that displays the animated text;
	JCheckBox[] activateAlarms = new JCheckBox[5];				//An Array of check boxes for activating or disactivating the five alarms;
	boolean[] isActivated = {true, true, true, true, true};		//An Array of boolean variables representing the state of alarms, initially activated;
	JPanel panel1 = new JPanel();		//A panel for holding alarm labels and times on the left of the main window;
	JPanel panel2 = new JPanel();		//A panel for holding prayer labels and times on the right of the main window;
	JPanel panel3 = new JPanel();		//A panel for holding the "set alarms to prayers" check box and the city label		
	JPanel panel4 = new JPanel();		//A panel for holding the analog clock;
	JPanel centerPanel = new JPanel();	//A panel representing the center area of the main window (it holds the time JLabel and the analog clock);
	JPanel southPanel = new JPanel();	//A panel representing the south area of the main window ( it holds panel3 and the animation label);
	JMenuBar menuBar = new JMenuBar();	//The menu bar of the main window;
	static Date clock;					//A Date object representing the current time;
	static Date[] alarms = new Date[5];	//5 Date objects representing the alarms;
	static String[] prayers = new String[5];	//5 Strings holding the names of prayers;
	static int[] prayersHours = new int[5];		//5 integers holding the hours of each prayer;
	static int[] prayersMinutes = new int[5];	//5 integers holding the minutes of each prayer;
	static BufferedReader prayersFile;			//The text file holding prayer times;
	AudioClip myAudio;							//An Audio object which is played as an alarm;
	ClockAnalogBuf analogClock = new ClockAnalogBuf();						//The analogClock that is used from the file ClockAnalogBuf.java;
	String day;									//A String holding the name of the day;
	static String animatedString;				//A String that holds the text to be animated;
	JLabel newLabel = new JLabel("", JLabel.CENTER);	//A label for displaying the time on a new frame;
	JFrame newFrame = new JFrame("The Time");			//A new frame for displaying the time only;
	
	//The Constructor =============================================================================
	public IslamicAlarmClock()
	{
		super("The Islamic Alarm Clock");	//creates the main frame
		clock = new Date();					//enitializes the clock to the current time
		
		//Change the LookAndFeel from "CrossPlatform" look and feel to "System" look and feel;
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e)
		{
			System.out.println("A problem in changing the LookAndFeel feature");
		}
		
		// finds the times of prayers in this city (initializes prayerHours and prayerMinutes); 
		findCity(theCity);
		
		time.setFont(new Font("Arial", Font.BOLD, 40));			//changes the font of the time label;
		time.setBorder(BorderFactory.createEtchedBorder());		//creates a border to the time label;
		date.setFont(new Font("Century Gothic", Font.BOLD, 20));//changes the font of the date label;
		date.setBorder(BorderFactory.createLineBorder(Color.blue));//creates a border to the date label;
		city.setBorder(BorderFactory.createLineBorder(Color.red));//creates a border to the city label;
		city.setFont(new Font("Castellar", Font.BOLD, 15));		  //changes the font of the city label
		
		//get a reference to the content pane of the main frame;
		Container cp = getContentPane();
		
		file.add(dispTime);			//adds the "dispTime" MenuItem to the file menu;
		file.addSeparator();		//adds separator;
		file.add(twelveFormat);		//adds the CheckBox "Tewlve Format" to the file menu;
		file.addSeparator();		//adds separator;
		file.add(exit);				//adds the "exit" MenuItem to the file menu;
		change.add(changeSound);	//adds the "changeSound" MenuItem to the change menu;
		change.add(changeAlarms);	//adds the "changeAlarms" MenuItem to the change menu;
		change.add(changeCity);		//adds the "changeCity" MenuItem to the change menu;
		help.add(instructions);		//adds the "instructions" MenuItem to the help menu;
		help.add(about);			//adds the "about" MenuItem to the help menu;
		menuBar.add(file);			//adds the file menu to the menuBar;
		menuBar.add(change);		//adds the change menu to the menuBar;
		menuBar.add(help);			//adds the help menu to the menuBar;
		setJMenuBar(menuBar);		//sets the MenuBar of the main frame to the "menuBar";
		file.setMnemonic('f');		//sets the Mnemonic of the file menu;
		change.setMnemonic('c');	//sets the Mnemonic of the change menu;
		help.setMnemonic('h');		//sets the Mnemonic of the help menu;
		
		//enializes the AudioClip to "makkahAdhan.wav" sound file;
		try
		{
			myAudio = Applet.newAudioClip(new URL("file://e:/sound files/makkahAdhan.wav"));
		}
		catch(java.net.MalformedURLException murle)
		{
			//displays an error frame
			
			final JFrame error = new JFrame("ERROR");
			JLabel msg = new JLabel("ERROR: The file \"makkahAdhan.wav\" cannot be opened!! Use Another file.");
			JButton ok = new JButton("OK");
			msg.setFont(new Font("Arial", Font.BOLD, 12));
			Container innerCp = error.getContentPane();
			innerCp.setLayout(new BorderLayout());
			innerCp.add(msg);
			JPanel panel = new JPanel();
			panel.add(ok);
			ok.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					error.hide();
				}
			});
			innerCp.add(panel, "South");
			error.setSize(400, 100);
			error.setLocation(100, 100);
			error.setVisible(true);
		}
		
		//sets the text of the time label to the current time using the clock object;
		time.setText(clock.getHours() + " : " + clock.getMinutes() + " : " + clock.getSeconds());
		
		cp.setLayout(new BorderLayout(10,10));			//sets the layout of the main frame to the BorderLayout;
		panel1.setLayout(new GridLayout(10, 1, 3, 3));	//sets the layout of the alarms panel to the GridLayout;
		panel2.setLayout(new GridLayout(10, 1, 3, 3));	//sets the layout of the prayer times panel to the GridLayout;
		panel3.setLayout(new FlowLayout(FlowLayout.RIGHT, 100, 1));	//sets the layout of the animation panel to the FlowLayout with right alignment;
		panel3.add(setAlarmsToPrayers);		//adds the "setAlarmsToPrayers" CheckBox to panel3
		panel3.add(city);					//adds the city label to panel3;
		panel4.add(analogClock.getContentPane());				// adds the content pane of the analogClock to panel 4; 
		centerPanel.setLayout(new BorderLayout());				// sets the layout of the centerPanel to BorderLayout;
		centerPanel.add(panel4, "Center");						// adds panel4 to the center of the centerPanel;
		centerPanel.add(time, "South");									// adds the time label to south of the centerPanel;
		southPanel.setLayout(new GridLayout(2, 1, 3, 3));		// sets the layout of the southPanel to GridLayout;
		animation.setFont(new Font("Comic Sans MS", Font.BOLD, 11)); //changes the font of the animation label;
		southPanel.add(animation);			//adds the animation label to the southPanel;
		southPanel.add(panel3);				//adds panel3 to the southPanel;
		cp.add(centerPanel, "Center");		//adds the centerPanel to the center of the main frame;
		cp.add(date, "North");				//adds the date label to the north of the main frame;
		
		//enitializing alarms to the current date and to a time of (00:00:00)
		for(int i = 0; i < alarms.length; i++)
			alarms[i] = new Date(clock.getYear(), clock.getMonth(), clock.getDate(), 0, 0, 0);
		
		//setting the text of alarm names labels and alarm times labels of the main frame and the "Change Alarms" frame;
		for(int j = 0; j < alarmLabels.length; j++)
		{
			//getting the hours of the current time, and formating them to the proper format;
			int hours = alarms[j].getHours() % format;
			if(hours == 0 && format == 12)
				hours = 12;
			if(format == 24 && alarms[j].getHours() == 12)
				hours = 12;
				
			//finding whether the current time is daytime or nighttime;
			if(format == 12 && alarms[j].getHours() > 11)
				amPm = "  PM";
			else if(format == 12 && alarms[j].getHours() <= 11)
			 	amPm = "  AM";
			else
				amPm = "";
			
			//sets the texts of the labels
			alarmTimes[j] = new JLabel(hours + " : " + alarms[j].getMinutes() + amPm, JLabel.CENTER);
			alarmLabels[j] = new JLabel("Alarm " + (j+1), JLabel.CENTER);
			innerAlarmLabels[j] = new JLabel("Alarm " + (j+1), JLabel.CENTER);
			innerAlarmLabels[j].setFont(new Font("Arial", Font.BOLD, 20)); 
			innerAlarmTimes[j] = new JLabel(hours + " : " + alarms[j].getMinutes() + amPm, JLabel.CENTER);  
			innerAlarmTimes[j].setFont(new Font("Arial", Font.BOLD, 30));
			
			//adds the alarm names and alarm times labels of the main frame to panel1
			panel1.add(alarmLabels[j]);
			panel1.add(alarmTimes[j]);
		}
		
		
		//sets the texts of the prayerLabels and prayerTime labels, and adds them to panel1
		for(int k = 0; k < prayerLabels.length; k++)
		{
			switch(k)
			{
				case 0:
				prayerLabels[k] = new JLabel("Fajr", JLabel.CENTER);
				break;
				case 1:
				prayerLabels[k] = new JLabel("Dhuhur", JLabel.CENTER);
				break;
				case 2:
				prayerLabels[k] = new JLabel("Asr", JLabel.CENTER);
				break;
				case 3:
				prayerLabels[k] = new JLabel("Maghrib", JLabel.CENTER);
				break;
				case 4:
				prayerLabels[k] = new JLabel("Eshaa", JLabel.CENTER);
				break;
			}
			
			//sets the texts of prayerTimes labels;
			prayerTimes[k] = new JLabel(prayersHours[k]%format + ":" + prayersMinutes[k], JLabel.CENTER);
			//adds prayerTimes labels and prayerLabels to panel2; 
			panel2.add(prayerLabels[k]);
			panel2.add(prayerTimes[k]);
		}
		
		cp.add(panel1, "West");		//adds panel1 to the west area of the main frame;
		cp.add(panel2, "East");		//adds panel2 to the east area of the main frame;
		cp.add(southPanel, "South");//adds panel3 to the south area of the main frame;
		setSize(500, 530);	//sets the size of the main frame;
		setVisible(true);	//displays the main frame;
		
		//deals with the main frame "WindowClosing" event;
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent we)
			{
				//displays confimation window;
				int answer = JOptionPane.showConfirmDialog(IslamicAlarmClock.this, "Are you sure you want to quit?");
				if(answer == JOptionPane.YES_OPTION)
					System.exit(0);
				else
					IslamicAlarmClock.this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			}
			
		});		
		
		//deals with the event of the "exit" MenuItem;		
		exit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				//displays confirmation window;
				int answer = JOptionPane.showConfirmDialog(IslamicAlarmClock.this, "Are you sure you want to quit?");
				if(answer == JOptionPane.YES_OPTION)
					System.exit(0);
				else
					IslamicAlarmClock.this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			}
		});
		
		//deals with the event of the "instructions" menuItem;
		instructions.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				final JFrame instructions = new JFrame("Instructions");
				JTextPane txt = new JTextPane();	//holds the instructions text;
				JButton ok = new JButton("OK");
				JPanel panel = new JPanel();
				
				//The istructions string;
				String info = "Welcome to The Islamic Alarm Clock 1.0 !!!\n\nFirst, we would like to thank you for choosing our software.. We appretiate your use of our software!!\n\n* Instructions:\n ===========\n1. To set the time and the date set your system time and date.. the clock will be set automatically.\n2. To change the alarms go to (change) from the menu bar, then (change alarms), then set the alarms to the desired time, then press OK.\nNOTE: To activate/disactivate any alarm, click on the check box next to that alarm. Alarms in black color are activated, while alarms in gray color are disactivates.\n3. To change the city go to (change) from the menu bar, then choose (change city), then select the desired city.\nNOTE: Number of cities added to this version (1.0) of The Islamic Alarm Clock is limited.\n4. To change the sound of your alarms go to (change) from the menu bar, then choose (change sound file), then choose one of the specified sound files, or one of your own. You can browse by pressing the browse button. After you finish press OK.\nIMPORTANT: sound files you can use should have a \".wav\", \".aiff\", or \".au\" extension, and they should have a bit rate of 40Kbps or higher.\n6. you can set the Time to 12 Hours format from the file menu --> use 12 Hours format.\n7. you can set the alarms to the prayer times from the check box (set alarms to prayers) underneath.\n7. You can display the time in a new window from the file menu --> Display Time in a New Window.\n8. To exit the program, go to the file menu --> quit.";
				
				//makes the txt not editalble
				txt.setEditable(false);
				//sets the background of txt to be the same as that of the frame;
				txt.setBackground(instructions.getBackground());
				txt.setText(info);
				Container cp = instructions.getContentPane();
				cp.setLayout(new BorderLayout());
				cp.add(txt, "Center");
				panel.add(ok);
				cp.add(panel, "South");
				
				//handles the event of the "OK" button;
				ok.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent ae)
					{
						instructions.setVisible(false);
					}
				});
				instructions.setSize(450, 480);
				instructions.setVisible(true);
			}
		});
		
		//handles the event of the "about" MenuItem;
		about.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				final JFrame about = new JFrame("About The Islamic Alarm Clock");
				JTextPane txt = new JTextPane(); //holds the "about" text;
				JButton ok = new JButton("OK");
				JPanel panel = new JPanel();
				//The "about" text;
				String info = "The Islamic Alarm Clock 1.0\n\nThe Islamic Alarm Clock 1.0 is a program developed and tested in KFUPM labs for educational purposes.\n\n**Author: AbdulRahman Mohammad Al-Mana.\n**Institution: King Fahd University of Petroleum and Minerals (KFUPM), College of Computer Science and Engineering (CCSE), Information and Computer Science Department (ICS).\nDhahran, Saudi Arabia.\n\n\nJan. 4, 2004\nAll Rights Reserved.\n\n\nNOTE: The code of the java application \"ClockAnalogBuf.java\" ,written by: Fred Swartz, was used in this application to produce the analog clock. The code is available on the web at:\nhttp://leepoint.net/notes-java/45examples/40animation/50analogclockbuf/analogclockbufexample.html";
				//makes the txt not editable; 
				txt.setEditable(false);
				//sets the background of txt to be the same as the frame;
				txt.setBackground(about.getBackground());
				txt.setText(info);
				Container cp = about.getContentPane();
				cp.setLayout(new BorderLayout());
				cp.add(txt, "Center");
				panel.add(ok);
				cp.add(panel, "South");
				
				//handles the event of the "OK" button;
				ok.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent ae)
					{
						about.setVisible(false);
					}
				});
				about.setSize(350, 450);
				about.setVisible(true);
			}
		});
				
		//handles the event of the "changeCity" MenuItem;
		changeCity.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				final JFrame changeCityFrame = new JFrame("Change City");

				//makes a combo box of city names;
				final JComboBox cities = new JComboBox();
				cities.addItem("Makkah");
				cities.addItem("Madinah");
				cities.addItem("Riyadh");
				cities.addItem("Jeddah");
				cities.addItem("Taif");
				cities.addItem("Dammam");
				cities.addItem("Tabouk");
				cities.addItem("Dhahran");
				cities.addItem("Khobar");
				cities.addItem("Abqaiq");
				cities.addItem("Jubail");
				cities.addItem("Abha");
				cities.addItem("Najran");
				cities.addItem("Jaizan");
				cities.addItem("Buraidah");
				cities.addItem("Unaizah");
				cities.addItem("Ha'il");
				cities.addItem("Yanbu");
				cities.addItem("Hofuf");
				
				//label for prompting the user to choose a city;
				JLabel label = new JLabel("Choose a city:", JLabel.CENTER);
				
				JPanel ctPanel = new JPanel();
				ctPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 80, 5));
				ctPanel.add(label);
				ctPanel.add(cities);
				JButton ok = new JButton("OK");
				JButton cancel = new JButton("Cancel");
				JPanel btnPanel = new JPanel();
				btnPanel.add(ok);
				btnPanel.add(cancel);
				
				Container citiesCp = changeCityFrame.getContentPane();
				citiesCp.setLayout(new GridLayout(2, 1));
				citiesCp.add(ctPanel);
				citiesCp.add(btnPanel);
				
				changeCityFrame.setSize(200, 200);
				changeCityFrame.setVisible(true);
				
				//handles the event of the "OK" button;
				ok.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent ae)
					{
						//gets the name of the selected city;
						String selectedCity = cities.getSelectedItem().toString();
						
						//finds the city;
						findCity(selectedCity);
						
						//setting the prayerTimes to the prayer times of this city;
						for(int i = 0; i < prayerTimes.length; i++)
						{
							int hours = prayersHours[i] % format;
							if( hours == 0 && format == 12)
								hours = 12;
							if(format == 24 && prayersHours[i] == 12)
								hours = 12;
							if(format == 12 && prayersHours[i] > 11)
								amPm = "  PM";
							else if(format == 12 && prayersHours[i] <= 11)
			 					amPm = "  AM";
			 				else
			 					amPm = "";
							prayerTimes[i].setText(hours + ":" + prayersMinutes[i] + amPm);
							if(setAlarmsToPrayers.isSelected())
							{
								alarms[i].setMinutes(prayersMinutes[i]);
								alarmTimes[i].setText(hours + " : " + alarms[i].getMinutes() + amPm); 
								innerAlarmTimes[i].setText(hours + " : " + alarms[i].getMinutes() + amPm);
							}
						}
						//changing the name of the city in the main window
						city.setText(selectedCity);
						changeCityFrame.setVisible(false);
					}
				});
				
				//handles the event of the "Cancel" button;
				cancel.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent ae)
					{
						changeCityFrame.setVisible(false);
					}
				});
			}
		});
		
		//handles the event of the "Twelve Format" CheckBox;
		twelveFormat.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				//changing the format if the check box is selected;
				if(twelveFormat.isSelected())
					format = 12;
				else
					format = 24;
				
				//modifies the texts of the labels holding alarm and prayer times in the main frame and the "Change Alarms" frame	
				for(int i = 0; i < prayerTimes.length; i++)
				{
					int hoursP = prayersHours[i] % format;
					if(hoursP == 0 && format == 12)
						hoursP = 12;
					if(format == 24 && prayersHours[i] == 12)
						hoursP = 12;
					if(format == 12 && prayersHours[i] > 11)
						amPm = "  PM";
					else if(format == 12 && prayersHours[i] <= 11)
			 			amPm = "  AM";
			 		else
			 			amPm = ""; 
					prayerTimes[i].setText(hoursP + ":" + prayersMinutes[i] + amPm);
					
					int hoursA = alarms[i].getHours() % format;
					if(hoursA == 0 && format == 12)
						hoursA = 12;
					if(format == 24 && alarms[i].getHours() == 12)
							hoursA = 12;
					if(format == 12 && alarms[i].getHours() > 11)
						amPm = "  PM";
					else if(format == 12 && alarms[i].getHours() <= 11)
			 			amPm = "  AM";
			 		else
			 			amPm = ""; 
					alarmTimes[i].setText(hoursA + " : " + alarms[i].getMinutes() + amPm);
					innerAlarmTimes[i].setText(hoursA + " : " + alarms[i].getMinutes() + amPm);
				}
				
				//repaints the main frame;
				IslamicAlarmClock.this.repaint();
			}
		});
				
						
		//handles the event of the "ChangeSoundFile" MenuItem;
		changeSound.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				final JFrame changeSoundFrame = new JFrame("Change Alarm Sound");
				
				//creates RadioButtons of predefined sound files
				final JRadioButton makkah = new JRadioButton("Makkah's Athan", true);// the true indicates that it is initially selected;
				final JRadioButton madinah = new JRadioButton("Al Madinah's Athan");
				final JRadioButton aqsa = new JRadioButton("Al Aqsa's Athan");
				final JRadioButton fajrMadinah = new JRadioButton("Fajr Al Madinah's Athan");
				
				//creates a RadioButton of "Others" option;
				final JRadioButton others = new JRadioButton("Others (Specify):");
				
				//creates a text field for typing the names of other files;
				final JTextField specified = new JTextField(30);
				
				//creates a text pane for holding information massege;
				JTextPane info = new JTextPane();
				info.setBackground(changeSoundFrame.getBackground());
				//set the text "info" to the information massege;
				info.setText("   NOTE:\tThe sound files this program supports are only those with \".wav\",\n\t \".aiff\", or \".au\" extensions and having bitrates greater than 20 kbps.");
				info.setEditable(false);
				JButton browse = new JButton("Browse"); //A browse button;
				JButton ok = new JButton("OK");
				JButton cancel = new JButton("Cancel");
				JPanel btnPanel = new JPanel();
				JPanel othersPanel = new JPanel();
				othersPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 80, 5));
				btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 80, 5));
				othersPanel.add(specified);
				othersPanel.add(browse);
				btnPanel.add(ok);
				btnPanel.add(cancel);
				
				//create a button group that groups the radio buttons together;
				ButtonGroup soundsButtons = new ButtonGroup();
				
				//adds the radio buttons to the button group
				soundsButtons.add(makkah);
				soundsButtons.add(madinah);
				soundsButtons.add(aqsa);
				soundsButtons.add(fajrMadinah);
				soundsButtons.add(others);
				
				
				Container cpSounds = changeSoundFrame.getContentPane();
				cpSounds.setLayout(new GridLayout(8, 1));
				cpSounds.add(makkah);
				cpSounds.add(madinah);
				cpSounds.add(aqsa);
				cpSounds.add(fajrMadinah);
				cpSounds.add(others);
				cpSounds.add(othersPanel);
				cpSounds.add(info);
				cpSounds.add(btnPanel);
				
				//An inner class that defines an action listener for changing the sound (i.e. pressing the "OK" button);;
				class SoundsListener implements ActionListener
				{
					public void actionPerformed(ActionEvent ae)
					{
						
						if(makkah.isSelected())
						{
							//Changes myAudio to the specified file;
							try
							{
								myAudio = Applet.newAudioClip(new URL("file://e:/sound files/makkahAdhan.wav"));
							}
							catch(java.net.MalformedURLException murle)
							{
								//displays an error frame;
								final JFrame error = new JFrame("ERROR");
								JLabel msg = new JLabel("ERROR: The file \"makkah.wav\" cannot be opened!! Use Another file.");
								JButton ok = new JButton("OK");
								msg.setFont(new Font("Arial", Font.BOLD, 12));
								Container cp = error.getContentPane();
								cp.setLayout(new BorderLayout());
								cp.add(msg);
								JPanel panel = new JPanel();
								panel.add(ok);
								ok.addActionListener(new ActionListener()
								{
									public void actionPerformed(ActionEvent ae)
									{
										error.hide();
									}
								});
								cp.add(panel, "South");
								error.setSize(400, 100);
								error.setLocation(100, 100);
								error.setVisible(true);
							}
						}
						else if(madinah.isSelected())
						{
							//Changes myAudio to the specified file;
							try
							{
								myAudio = Applet.newAudioClip(new URL("file://e:/sound files/madinahAdhan.wav"));
							}
							catch(java.net.MalformedURLException murle)
							{
								//displays an error frame;
								final JFrame error = new JFrame("ERROR");
								JLabel msg = new JLabel("ERROR: The file \"madinahAdhan.wav\" cannot be opened!! Use Another file.");
								JButton ok = new JButton("OK");
								msg.setFont(new Font("Arial", Font.BOLD, 12));
								Container cp = error.getContentPane();
								cp.setLayout(new BorderLayout());
								cp.add(msg);
								JPanel panel = new JPanel();
								panel.add(ok);
								ok.addActionListener(new ActionListener()
								{
									public void actionPerformed(ActionEvent ae)
									{
										error.hide();
									}
								});
								cp.add(panel, "South");
								error.setSize(400, 100);
								error.setLocation(100, 100);
								error.setVisible(true);
							}
						}
						
						else if(aqsa.isSelected())
						{
							//Changes myAudio to the specified file;
							try
							{
								myAudio = Applet.newAudioClip(new URL("file://e:/sound files/alaqsaAdhan.wav"));
							}
							catch(java.net.MalformedURLException murle)
							{
								//displays an error frame;
								final JFrame error = new JFrame("ERROR");
								JLabel msg = new JLabel("ERROR: The file \"alaqsaAdhan.wav\" cannot be opened!! Use Another file.");
								JButton ok = new JButton("OK");
								msg.setFont(new Font("Arial", Font.BOLD, 12));
								Container cp = error.getContentPane();
								cp.setLayout(new BorderLayout());
								cp.add(msg);
								JPanel panel = new JPanel();
								panel.add(ok);
								ok.addActionListener(new ActionListener()
								{
									public void actionPerformed(ActionEvent ae)
									{
										error.hide();
									}
								});
								cp.add(panel, "South");
								error.setSize(400, 100);
								error.setLocation(100, 100);
								error.setVisible(true);
							

							}
						}
						
						else if(fajrMadinah.isSelected())
						{
							//Changes myAudio to the specified file;
							try
							{
								myAudio = Applet.newAudioClip(new URL("file://e:/sound files/fajrMadinah.wav"));
							}
							catch(java.net.MalformedURLException murle)
							{
								//displays an error frame;
								final JFrame error = new JFrame("ERROR");
								JLabel msg = new JLabel("ERROR: The file \"fajrMadinah.wav\" cannot be opened!! Use Another file.");
								JButton ok = new JButton("OK");
								msg.setFont(new Font("Arial", Font.BOLD, 12));
								Container cp = error.getContentPane();
								cp.setLayout(new BorderLayout());
								cp.add(msg);
								JPanel panel = new JPanel();
								panel.add(ok);
								ok.addActionListener(new ActionListener()
								{
									public void actionPerformed(ActionEvent ae)
									{
										error.hide();
									}
								});
								cp.add(panel, "South");
								error.setSize(400, 100);
								error.setLocation(100, 100);
								error.setVisible(true);

							}
						}
						
						else if(others.isSelected())
						{
							//Gets the typed file name and tokenizes it to make a valid file name for myAudio; 
							String soundFile = specified.getText().trim();
							StringTokenizer tokenizer = new StringTokenizer(soundFile, "\\");
							soundFile = "";
							while(tokenizer.hasMoreTokens())
								soundFile += (tokenizer.nextToken() + "/");
							if(!soundFile.equals(""))
							soundFile = soundFile.substring(0, soundFile.lastIndexOf('/'));
							
							//Changes myAudio to the specified file;	
							try
							{
								myAudio = Applet.newAudioClip(new URL("file://" + soundFile));
							}
							catch(java.net.MalformedURLException murle)
							{
								//displays an error frame
								final JFrame error = new JFrame("ERROR");
								JLabel msg = new JLabel("The File \"" + soundFile + "\" cannot be opened!! Use another file");
								JButton ok = new JButton("OK");
								msg.setFont(new Font("Arial", Font.BOLD, 12));
								Container cp = error.getContentPane();
								cp.setLayout(new BorderLayout());
								cp.add(msg);
								JPanel panel = new JPanel();
								panel.add(ok);
								ok.addActionListener(new ActionListener()
								{
									public void actionPerformed(ActionEvent ae)
									{
										error.hide();
									}
								});
								cp.add(panel, "South");
								error.setSize(400, 100);
								error.setLocation(100, 100);
								error.setVisible(true);
									
							}
							
						}
						
						//handles the case when the user press "OK", selecting "others" and typing nothing
						if(others.isSelected() && specified.getText().trim().equals(""))
						{
							Toolkit.getDefaultToolkit().beep();
							final JFrame error = new JFrame("ERROR");
							JLabel msg = new JLabel("You must specify a file!!", JLabel.CENTER);
							JButton ok = new JButton("OK");
							msg.setFont(new Font("Arial", Font.BOLD, 12));
							Container cp = error.getContentPane();
							cp.setLayout(new BorderLayout());
							cp.add(msg);
							JPanel panel = new JPanel();
							panel.add(ok);
							ok.addActionListener(new ActionListener()
							{
								public void actionPerformed(ActionEvent ae)
								{
									error.hide();
								}
							});
							cp.add(panel, "South");
							error.setSize(250, 100);
							error.setLocation(100, 100);
							error.setVisible(true);
						}
						
						//handles the case when the user types a file having a format different form ".wav", ".aiff", and ".au";
						else if(others.isSelected() && !specified.getText().trim().endsWith(".wav") && !specified.getText().trim().endsWith(".WAV") && !specified.getText().trim().endsWith(".aiff") && !specified.getText().trim().endsWith(".AIFF") && !specified.getText().trim().endsWith(".au") && !specified.getText().trim().endsWith(".AU"))
						{
							Toolkit.getDefaultToolkit().beep();
							final JFrame error = new JFrame("ERROR");
							JLabel msg = new JLabel("The sound file must have one of the following extensions: \".wav\", \".aiff\", or \".au\"!! Use another sound file.", JLabel.CENTER);
							JButton ok = new JButton("OK");
							msg.setFont(new Font("Arial", Font.BOLD, 12));
							Container cp = error.getContentPane();
							cp.setLayout(new BorderLayout());
							cp.add(msg);
							JPanel panel = new JPanel();
							panel.add(ok);
							ok.addActionListener(new ActionListener()
							{
								public void actionPerformed(ActionEvent ae)
								{
									error.hide();
								}
							});
							cp.add(panel, "South");
							error.setSize(700, 100);
							error.setLocation(100, 100);
							error.setVisible(true);
						}	
						else
						{
							changeSoundFrame.setVisible(false);
							repaint();
						}
					}
				}
				
				//create an actionListener object from the inner local class "SoundListener" and adding it to the button "OK";		
				ok.addActionListener(new SoundsListener());
				
				//handles the event of the "Browse" button;
				browse.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent ae)
					{
						FileDialog fd = new FileDialog(changeSoundFrame);
						fd.setVisible(true);
						specified.setText(fd.getDirectory() + fd.getFile());
					}
				});
				
				
				//handles the event of the "Cancel" button;
				cancel.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent ae)
					{
						changeSoundFrame.setVisible(false);
						repaint();
					}
				});
								
					
				changeSoundFrame.setVisible(true);
				changeSoundFrame.pack();
			}
		});
		
		//handles the event of the "changeAlrams" MenuItem;
		changeAlarms.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				final JFrame ChangeAlarms = new JFrame("Change Alarms");
				
				//panels used for organizing the layout of the "ChangeAlarms" frame;
				JPanel[] myPanels = new JPanel[5];
				JPanel[] smallPanels = new JPanel[5];
				JPanel[] bigPanels = new JPanel[5];
				JPanel hugePanel1 = new JPanel();
				JPanel hugePanel2 = new JPanel();
				JPanel okCancel = new JPanel();
				
				
				Container cp = ChangeAlarms.getContentPane();
				cp.setLayout(new GridLayout(3, 1, 5, 5));
				
				//Add and Subtract buttons;
				JButton[] buttons = new JButton[20];
				
				JButton ok = new JButton("OK");
				JButton cancel = new JButton("Cancel");
				
				//Temporary alarms that holds the values of the current alarms;
				final Date[] tempAlarms = new Date[5];
				for(int i = 0; i < tempAlarms.length; i++)
					tempAlarms[i] = new Date(alarms[i].getYear(), alarms[i].getMonth(), alarms[i].getDate(), alarms[i].getHours(), alarms[i].getMinutes(), alarms[i].getSeconds());	
					
				//creates the Add/Subtract buttons;	
				for(int i = 0; i < buttons.length; i++)
				{
					if(i%2 == 0)
						buttons[i] = new JButton("^");
					else
						buttons[i] = new JButton("v");
				}
				
				//create "myPanel" panels, adds the Add/Subtract buttons to myPanel, and handles their events;
				for(int i = 0; i < alarmLabels.length; i++)
				{
					//creates the panel;
					myPanels[i] = new JPanel();
					
					//Adds the buttons to myPanel, and handles their events;
					for(int k = 0; k < 4; k++)
					{
						//entegers that holds the number of the iteration;
						final int b = i;
						final int c = k;
						
						//The formula (4*i + k) is used to refer to all the 20 Add/Subtract buttons, in a manner of 4 button per 1 panel;
						myPanels[i].add(buttons[4*i + k]);
						
						//handles the event of this specific button;
						buttons[4*i + k].addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent ae)
							{
								//The integer c, which is the number of inner iteration, is used to distinguish between four types of buttons(add hours, subtract hours, add minutes, subract minutes);
								switch(c)
								{
									case 0:
									alarms[b].setHours((alarms[b].getHours() + 1) % 24);
									break;
									case 1:
									alarms[b].setHours((alarms[b].getHours() - 1) % 24); 
									break;
									case 2:
									alarms[b].setMinutes((alarms[b].getMinutes() + 1)%60);
									break;
									case 3:
									alarms[b].setMinutes((alarms[b].getMinutes() - 1)%60);
									break;
								}
								
								//modifies the alarmTimes, and the innerAlarmTimes texts;
								int hours = alarms[b].getHours() % format;
								if(hours == 0 && format == 12)
									hours = 12;
								if(format == 24 && alarms[b].getHours() == 12)
									hours = 12;
								if(format == 12 && alarms[b].getHours() > 11)
									amPm = "  PM";
								else if(format == 12 && alarms[b].getHours() <= 11)
			 						amPm = "  AM";
			 					else
			 						amPm = "";
								innerAlarmTimes[b].setText(hours + " : " + alarms[b].getMinutes() + amPm);
								innerAlarmLabels[b].setText("Alarm " + (b+1));
								alarmTimes[b].setText(hours + " : " + alarms[b].getMinutes() + amPm);

							}
						});
					}
				}
				
				okCancel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 50));
				okCancel.add(ok);
				okCancel.add(cancel);
				
				//handles the event of the "OK" button;
				ok.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent ae)
					{	
						ChangeAlarms.setVisible(false);
					}
				});
				
				//handles the event of the "Cancel" button;	
				cancel.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent ae)
					{
						for(int i = 0; i < innerAlarmTimes.length; i++)
						{
							//Assignes the alarms to their original values; 
							alarms[i] = tempAlarms[i];
							int hours = tempAlarms[i].getHours() % format;
							if(hours == 0 && format == 12)
								hours = 12;
							if(format == 24 && alarms[i].getHours() == 12)
								hours = 12;
							if(format == 12 && tempAlarms[i].getHours() > 11)
								amPm = "  PM";
							else if(format == 12 && tempAlarms[i].getHours() <= 11)
			 					amPm = "  AM";
			 				else
			 					amPm = "";
							alarmTimes[i].setText(hours + " : " + alarms[i].getMinutes() + amPm);
							innerAlarmTimes[i].setText(hours + " : " + alarms[i].getMinutes() + amPm);
						}
						ChangeAlarms.setVisible(false);
					}
				});
				
				//handles the case when the user choose to exit whithout confirming whether to agree on the changes in the alarms(i.e. pressing "OK") or keep the alarms as they were(i.e. pressing "Cancel"); 
				ChangeAlarms.addWindowListener(new WindowAdapter()
				{
					public void windowClosing(WindowEvent we)
					{
							//gives a sound for this error;
							Toolkit.getDefaultToolkit().beep();
							
							ChangeAlarms.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
							final JFrame error = new JFrame("ERROR");
							JButton ok = new JButton("OK");
							JLabel msg = new JLabel("Press (OK) to change the alarms or (Cancel) to retain the previous alarms", JLabel.CENTER);
							msg.setFont(new Font("Arial", Font.BOLD, 12));
							Container cp = error.getContentPane();
							cp.setLayout(new BorderLayout());
							cp.add(msg, "Center");
							JPanel panel = new JPanel();
							panel.add(ok);
							ok.addActionListener(new ActionListener()
							{
								public void actionPerformed(ActionEvent ae)
								{
									error.hide();
								}
							});
							cp.add(panel, "South");
							error.setSize(500, 100);
							error.setLocation(100, 100);
							error.setVisible(true);
					}
				});
						
						
				//Adds the panels to the "changeAlarms" frame;	
				for(int i = 0; i < myPanels.length; i++)
				{
					activateAlarms[i] = new JCheckBox("", isActivated[i]);
					smallPanels[i] = new JPanel();
					bigPanels[i] = new JPanel();
					bigPanels[i].setLayout(new GridLayout(3, 1, 5, 5));
					bigPanels[i].setBorder(BorderFactory.createLineBorder(Color.red));
					smallPanels[i].add(activateAlarms[i]);
					smallPanels[i].add(innerAlarmLabels[i]);
					bigPanels[i].add(smallPanels[i]);
					bigPanels[i].add(innerAlarmTimes[i]);
					bigPanels[i].add(myPanels[i]);
					
					//Adds the first three panels to the hugePanel1, and the latter two to the hugePanel2;
					if(i < 3)
						hugePanel1.add(bigPanels[i]);
					else
						hugePanel2.add(bigPanels[i]);
				}
				
				//handles the events of the activate alarms check boxes
				for(int i = 0; i < activateAlarms.length; i++)
				{
					final int b = i;
					activateAlarms[b].addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent ae)
						{
							if(activateAlarms[b].isSelected())
							{
								isActivated[b] = true;
								
								//Changes the color [to black] of the font of the alarmLabels and alarmTimes in both the main frame and the "ChangeAlarms" frame;
								alarmLabels[b].setForeground(Color.black);
								alarmTimes[b].setForeground(Color.black);
								innerAlarmLabels[b].setForeground(Color.black);
								innerAlarmTimes[b].setForeground(Color.black);
								ChangeAlarms.repaint();
								IslamicAlarmClock.this.repaint();
							}
							else
							{
								isActivated[b] = false;
								
								//Changes the color [to gray] of the font of the alarmLabels and alarmTimes in both the main frame and the "changeAlarms" frame;
								alarmLabels[b].setForeground(Color.gray);
								alarmTimes[b].setForeground(Color.gray);
								innerAlarmLabels[b].setForeground(Color.gray);
								innerAlarmTimes[b].setForeground(Color.gray);
								ChangeAlarms.repaint();
								IslamicAlarmClock.this.repaint();
							}
						}
					});
				}
				
				//Adds the two huge panels, and the "okCancel" panel to the "changeAlarms" frame; 				
				cp.add(hugePanel1);
				cp.add(hugePanel2);
				cp.add(okCancel);
				ChangeAlarms.setVisible(true);
				ChangeAlarms.pack();
				IslamicAlarmClock.this.repaint();
				
			}
		});
		
		//handles the event of the "setAlarmsToPrayers" check box;
		setAlarmsToPrayers.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if(setAlarmsToPrayers.isSelected())
				{
					//modifies the alarms, alarmTimes, and innerAlarmTimes to the times of prayers;
					for(int i = 0; i < alarms.length; i++)
					{ 
						alarms[i].setHours(prayersHours[i]);
						int hours = alarms[i].getHours() % format;
						if(hours == 0 && format == 12)
							hours = 12;
						if(format == 24 && alarms[i].getHours() == 12)
							hours = 12;
						if(format == 12 && alarms[i].getHours() > 11)
							amPm = "  PM";
						else if(format == 12 && alarms[i].getHours() <= 11)
			 				amPm = "  AM";
			 			else 
			 			    amPm = "";
						alarms[i].setMinutes(prayersMinutes[i]);
						alarmTimes[i].setText(hours + " : " + alarms[i].getMinutes() + amPm); 
						innerAlarmTimes[i].setText(hours + " : " + alarms[i].getMinutes() + amPm);  
					}
				}
				else
				{
					//modifies the alarms to inital values of (00:00), and modifies the alarmTimes and the innerAlarmTimes;
					for(int i = 0; i < alarms.length; i++)
					{
						alarms[i].setHours(0);
						alarms[i].setMinutes(0);
						int hours = alarms[i].getHours() % format;
						if(hours == 0 && format == 12)
							hours = 12;
						if(format == 24 && alarms[i].getHours() == 12)
							hours = 12;
						if(format == 12 && alarms[i].getHours() > 11)
							amPm = "  PM";
						else if(format == 12 && alarms[i].getHours() <= 11)
			 				amPm = "  AM";
			 			else
			 				amPm = "";
						
						alarmTimes[i].setText(hours + " : " + alarms[i].getMinutes() + amPm); 
						innerAlarmTimes[i].setText(hours + " : " + alarms[i].getMinutes() + amPm);  
					}
				}
					
				IslamicAlarmClock.this.repaint();
			}
			
		});
		
		//deals with the event of the "dispTime" MenuItem;
		dispTime.addActionListener(new TimeDispListener());
		
		//enitializes the animatedString;
		animatedString = " * Today  :   " + date.getText() + "     *-*-*-*-*-*-*-*-*       Prayer   Times   Today  in   [ " + city.getText() + " ]  *-*-*-*-*-*-*-*-*-*     Fajr:    [ " + prayerTimes[0].getText() + " ]      *-*-*-*-*       Dhuhur:    [ " + prayerTimes[1].getText() + " ]      *-*-*-*-*       Asr:   [ " + prayerTimes[2].getText() + " ]      *-*-*-*-*       Maghrib:   [ " + prayerTimes[3].getText() + " ]      *-*-*-*-*       Eshaa:   [ " + prayerTimes[4].getText() + " ]      *-*-*-*-*-*-*-*-*-*-*        Times of Alarms     *-*-*-*-*-*-*          Alarm 1:    [ " + alarmTimes[0].getText() + " ]       *-*-*-*        Alarm 2:    [ "  + alarmTimes[1].getText() + " ]       *-*-*-*        Alarm 3:    [ "  + alarmTimes[2].getText() + " ]       *-*-*-*        Alarm 4:    [ " + alarmTimes[3].getText() + " ]       *-*-*-*        Alarm 5:    [ " + alarmTimes[4].getText() + " ]                    *-*-*-*-*-*-*-*-*-*-*-*-*-*                                                                                                 ";
		
		//creates a thread and passes an instance of the class Animator to it; 
		Thread thr1 = new Thread(new Animator());
		//starts the thread;
		thr1.start();
		
		//refrshes the time;
		refreshTime();
		
	}//end of Constructor
	
	//================================================================================================
	
	/*
	 *	A method that refreshes the time in an infinite loop;
	 *	 
	 *	Parameters: none;
	 *	
	 *	Return:		void;
	 *		
	 *	Exceptions:		InterruptedException: in case that a sleep thread is invoked;
	 *
	*/
	public void refreshTime()
	{	
		//enfinite loop;
		for(int d = 0; d > -1; d++)
		{
			//calls a thread to sleep every 1 second;
			try
			{
				Thread.sleep(1000);
			}
			catch(InterruptedException e)
			{
				//displays an error window;
				JFrame error = new JFrame("Thread Error");
				JTextField msg = new JTextField(50);
				msg.setText("Some Error related to Threads has occured!!");
				error.getContentPane().add(msg);
				error.pack();
				error.setVisible(true);
			}
			//refreshes the clock;
			clock = new Date();
			
			//modifies the text of the time label;
			int hours = clock.getHours();
			if(hours == 0 && format == 12)
				hours = 12;
			else if(format == 12 && hours != 12)
				hours %= 12;
			if(format == 12 && clock.getHours() > 11)
				amPm = "  PM";
			else if(format == 12 && clock.getHours() <= 11)
			 	amPm = "  AM";
			else 
				amPm = "";
			time.setText(hours + " : " + clock.getMinutes() + " : " + clock.getSeconds() + amPm);
			
			//finds the day of the week;
			switch(clock.getDay())
			{
				case 0:
				day = "Sunday";
				break;
				case 1:
				day = "Monday";
				break;
				case 2:
				day = "Tuesday";
				break;
				case 3:
				day = "Wednesday";
				break;
				case 4:
				day = "Thursday";
				break;
				case 5:
				day = "Friday";
				break;
				case 6:
				day = "Saturday";
				break;
			}
			
			//modifies the text of the date label;
			date.setText(day + "      " + clock.getDate() + "  " + (clock.getMonth()+1) + "  " + (clock.getYear()+1900) + " AD");
			
			//Checks if the current time is equal to one of the alarms;
			for(int i = 0; i < alarms.length; i++)
			if(clock.getHours() == alarms[i].getHours() && clock.getMinutes() == alarms[i].getMinutes() && clock.getSeconds() == alarms[i].getSeconds() && isActivated[i])
				{
						//plays myAudio; 
						myAudio.play();
						
						//displays an attention window;
						final JFrame alarmPlayed = new JFrame("Alarm " + (i+1) );
						JLabel msg = new JLabel("Alarm (" + (i+1) + ") is Playing now!!", JLabel.CENTER);
						JButton ok = new JButton("OK");
						JButton stop = new JButton("Stop");
						msg.setFont(new Font("Arial", Font.BOLD, 12));
						Container cp = alarmPlayed.getContentPane();
						cp.setLayout(new BorderLayout());
						cp.add(msg);
						JPanel panel = new JPanel();
						panel.add(ok);
						panel.add(stop);
						
						//handles the event of the "OK" button;
						ok.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent ae)
							{
								alarmPlayed.hide();
							}
						});
						
						//handles the event of the "stop" button;
						stop.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent ae)
							{
								//stops myAudio;
								myAudio.stop();
								alarmPlayed.hide();
							}
						});
						cp.add(panel, "South");
						alarmPlayed.setSize(200, 100);
						alarmPlayed.setLocation(100, 100);
						alarmPlayed.setVisible(true);
				}
				
				//refreshes and repaints the frame of the new frame displaying the time only;
				newLabel.setText(time.getText());
				newFrame.repaint();
				
				//repaints the main frame;
				repaint();
		}
	}//end of refresh() method
	//=================================================================================================
	
	/*
	 *A static method that finds the times of prayers in a specific city from a text file;
	 *
	 *  Parameters: A String representing the name of the city to be found;
	 *	 	
	 *	Return:		void;
	 *		
	 *	Exceptions:		FileNotFoundException: in case that the file is not found;
	 *					IOException: in a case that an error occures while reading from the file, or closing the reading stream; 
	 *					NumberFormatException: if the string to be parsed to numbers is not a string of numbers;
	 *
	 */
	
	public static void findCity(String city)
	{
		
		//boolean variables that specify the case of whether a city, a month or a day is found or not;
		boolean cityFound = false;
		boolean monthFound = false;
		boolean dayFound = false;
		
		//opens the file for reading;
		try
		{
			 prayersFile = new BufferedReader(new FileReader("ThePrayers.txt"));
		}
		catch(FileNotFoundException e)
		{
			//displays an error message;
			System.out.println("The \"thePrayers.txt\" file is not found!!");
		}
							
		try
		{
			//reads the first statement in the file;
			String line = prayersFile.readLine();
			
			//A loop for finding the city;
			while(line != null && !cityFound)
			{
				//tokenizes the line read;
				StringTokenizer tokenizer = new StringTokenizer(line, " \n,");
				
				//A loop for finding the name of the city in the line
				while(tokenizer.hasMoreTokens() && !cityFound)
				{
					if(tokenizer.nextToken().equals(city))
						cityFound = true;
				}
				line = prayersFile.readLine();
			}
				
			if(cityFound)
			{
				//A String for holding the name of the current month;
				String month = "";
				switch(clock.getMonth())
				{
					case 0:
					month = "January";
					break;
					case 1:
					month = "February";
					break;
					case 2:
					month = "March";
					break;
					case 3:
					month = "April";
					break;
					case 4:
					month = "May";
					break;
					case 5:
					month = "June";
					break;
					case 6:
					month = "July";
					break;
					case 7:
					month = "August";
					break;
					case 8:
					month = "September";
					break;
					case 9:
					month = "October";
					break;
					case 10:
					month = "November";
					break;
					case 11:
					month = "December";
					break;
				}
				
				line = prayersFile.readLine();
				
				//A loop for finding the month in the specific city;
				while(line != null && !monthFound)
				{
					//tokenizes the line read;
					StringTokenizer tokenizer = new StringTokenizer(line, " \n,");
					while(tokenizer.hasMoreTokens() && !monthFound)
					{
						if(tokenizer.nextToken().equals(month))
							monthFound = true;
					}
					line = prayersFile.readLine();
				}
			}
				
				if(monthFound)
				{
					//A String holding the current date;
					String theDate = clock.getDate() + "";
					
					line = prayersFile.readLine();
					
					//A loop for finding the date;
					while(line != null && !dayFound)
					{
						//tokenizes the line read;
						StringTokenizer tokenizer = new StringTokenizer(line, " \n,");
						while(tokenizer.hasMoreTokens())
						{
							//gets the times of prayers if the date is found;
							if(tokenizer.nextToken().equals(theDate))
							{
								dayFound = true;
								prayers[0] = tokenizer.nextToken();
								tokenizer.nextToken();
								prayers[1] = tokenizer.nextToken();
								prayers[2] = tokenizer.nextToken();
								prayers[3] = tokenizer.nextToken();
								prayers[4] = tokenizer.nextToken();
							}
						}
						line = prayersFile.readLine();
					}
				}
			}
			catch(IOException e)
			{
				System.out.println("A problem in reading the \"thePrayers.txt\" file!!");
			}
			
			try
			{
				//A tokenizer for separating the hours from the minutes in the text of each prayers label; 
				StringTokenizer tokenizer;
				for(int i = 0; i < prayers.length; i++)
				{
					tokenizer = new StringTokenizer(prayers[i], ":");
					
					//Parses the hours from the prayers label to the prayersHours;
					prayersHours[i] = Integer.parseInt(tokenizer.nextToken());
					
					//Parses the minutes from the prayers label to the prayersMinutes;
					prayersMinutes[i] = Integer.parseInt(tokenizer.nextToken());
					
					//changes the format of the hours to the default format (24 Hrs);
					if(i > 0 && prayersHours[i] < 11)
						prayersHours[i] += 12;
				}
			}
			catch(NumberFormatException e)
			{
				System.out.println("A problem in retrieving the times of prayers form the \"thePrayers.txt\" file");
			}
			
			//closes the prayersFile stream;
			try
			{
				prayersFile.close();
			}
			catch(IOException e)
			{
				System.out.println("A problem in closing the file \"thePrayers.txt\" !!");
			}
		}//end of findCity() method
		//==============================================================================================

	//THE MAIN METHOD:
	public static void main(String[] args)
	{
		new IslamicAlarmClock();
				
	}//end of the main;
	//==================================================================================================
	
	//An inner class that implements the actions needed for animating a String;
	class Animator extends Thread
	{
		//The run method;
		public void run()
		{
			//An integer representing the start index of the substring to be animated;
			int start = 0;
			
			//enfinite loop;
			for(int d = 0; d > -1; d++)
			{
				try
				{
					//calls the thread to sleep every 200ms
					Thread.sleep(200);
					
					//refreshes the animated string;
					animatedString = " * Today  :   " + date.getText() + "     *-*-*-*-*-*-*-*-*       Prayer   Times   Today  in   [ " + city.getText() + " ]  *-*-*-*-*-*-*-*-*-*     Fajr:    [ " + prayerTimes[0].getText() + " ]      *-*-*-*-*       Dhuhur:    [ " + prayerTimes[1].getText() + " ]      *-*-*-*-*       Asr:   [ " + prayerTimes[2].getText() + " ]      *-*-*-*-*       Maghrib:   [ " + prayerTimes[3].getText() + " ]      *-*-*-*-*       Eshaa:   [ " + prayerTimes[4].getText() + " ]      *-*-*-*-*-*-*-*-*-*-*        Times of Alarms     *-*-*-*-*-*-*          Alarm 1:    [ " + alarmTimes[0].getText() + " ]       *-*-*-*        Alarm 2:    [ "  + alarmTimes[1].getText() + " ]       *-*-*-*        Alarm 3:    [ "  + alarmTimes[2].getText() + " ]       *-*-*-*        Alarm 4:    [ " + alarmTimes[3].getText() + " ]       *-*-*-*        Alarm 5:    [ " + alarmTimes[4].getText() + " ]                    *-*-*-*-*-*-*-*-*-*-*-*-*-*                                                                                                 ";
					
					//A String representing the segment of the animatedString to be displayed;
					String anim;
					
					//A condition for moving the start index(making the length of the displayed string less than or equal to 80);
					if(d%animatedString.length() > 80 && animatedString.length() != 80)
						start++;
						
					//enitializes the anim String to the substirng to be diplayed;
					if(d%animatedString.length() > start)//checks if lastIndex > firstIndex
						animation.setText(animatedString.substring(start, d%animatedString.length()));
						
					//Assigns start to 0 if start > lastIndex;
					if(d%animatedString.length() <= start)
						start = 0;
						
					//repaints the main frame;
					repaint(); 
				}
				
				catch(InterruptedException e)
				{
					System.out.println("A problem with a sleeping thread!!");
				}
			}
		}//end of run();
	}//end of Animator inner class;
//=========================================================================	
	//An inner class that defines a listener to the "dispTime" MenuItem;
	class TimeDispListener implements ActionListener
	{
		public void actionPerformed(ActionEvent ae)
		{
			newLabel.setFont(time.getFont());
			newFrame.getContentPane().add(newLabel);
			newFrame.setSize(300, 120);
			newFrame.setVisible(true);			
		}//end of actionPerformed method;
	}//end of TimeDispListener inner class;
//==========================================================================				
}//end of IslamicAlarmClock class;
