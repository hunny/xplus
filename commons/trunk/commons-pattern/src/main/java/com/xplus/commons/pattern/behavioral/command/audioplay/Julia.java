package com.xplus.commons.pattern.behavioral.command.audioplay;

/**
 * 客户端角色，由茱丽小女孩扮演
 */
public class Julia {

  public static void main(String[] args) {
    // 创建接收者对象
    AudioPlayer audioPlayer = new AudioPlayer();
    // 创建命令对象
    Command playCommand = new PlayCommand(audioPlayer);
    Command rewindCommand = new RewindCommand(audioPlayer);
    Command stopCommand = new StopCommand(audioPlayer);
    // 创建请求者对象
    Keypad keypad = new Keypad();
    keypad.setPlayCommand(playCommand);
    keypad.setRewindCommand(rewindCommand);
    keypad.setStopCommand(stopCommand);
    // 客户端调用测试
    keypad.play();
    keypad.rewind();
    keypad.stop();
    keypad.play();
    keypad.stop();
  }

}
