//install graphics builder and dependencies found here:
//http://groovy.codehaus.org/GraphicsBuilder
//add to classpath in one of the manners found here
//http://groovy.codehaus.org/Running#Running-Addingthingstotheclasspath

import groovy.swing.SwingBuilder  
import groovy.swing.j2d.*
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Font

import java.awt.Color

import static javax.swing.JFrame.EXIT_ON_CLOSE
  
def gb = new GraphicsBuilder()
def width = 200
def height = 200
def horiz_lines = 10
def vert_lines = 10
def avail_chars = ('a'..'z')
def random = new Random()
def entities = []

def gp = new GraphicsPanel(){
    public void paintComponent(Graphics g){
      super.paintComponent(g)
      Graphics2D g2 = (Graphics2D)g;
      g2.setColor(Color.BLACK);
      g2.fillRect(0, 0, getWidth(), getHeight());
      g2.setColor(Color.GREEN)
      g2.setFont(new Font("default", Font.BOLD, 16));
      entities.each{
        g2.drawString(avail_chars[it.character],it.x.intValue(),it.y.intValue());
    }
    }

    public void refresh(){
        super.repaint()
    }
}

class Entity{
    def x
    def y
    def vel
    def character
    def height
    def update_limit
    def last_update
    def Entity(x, y, vel, random, height){
        this.x = x
        this.y = y
        this.vel = vel
        this.height = height
        this.character = random.nextInt(26)
        update_limit = random.nextInt(50)+10
        last_update = update_limit
    }

    def update(characters){
        last_update -= 1
        if(last_update==0){
          character = character == characters.size-1 ? 0 : character+1
          last_update = update_limit
        }
        y = y >= height+10 ? -10 : y + vel
    }
}

vert_lines.times{vert ->
    horiz_lines.times{horiz ->
      def vel = random.nextInt(3) + 1
      entities << new Entity(width/vert_lines*vert, height/horiz_lines*horiz, vel, random, height)
    }
}
  
SwingBuilder.build {
   frame( title: 'GraphicsBuilder', size: [width,height],
          defaultCloseOperation: EXIT_ON_CLOSE, visible: true ){  
      panel( gp, graphicsOperation: null)
   }
}

while(true){
    sleep(30)
    entities.each{
      it.update(avail_chars)
    }
    gp.refresh()
}
