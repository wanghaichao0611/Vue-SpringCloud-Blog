<template>
    <div class="row clearfix">
      <div class="col-md-7 column">
        <Card style="width:500px">
          <div style="text-align: left">
            <span>消息提醒（关闭后，消息将不再进行提醒）</span>
            <br>
            <br>
            <RadioGroup v-model="msgOn" size="large">
              <Radio label="开启" ></Radio>
              &nbsp;&nbsp;
              <Radio label="关闭" ></Radio>
            </RadioGroup>
          </div>
        </Card>
        <br>
        <Card style="width:500px">
          <div style="text-align: left">
            <span>回复我的消息提醒（接受谁的评论消息提醒）</span>
            <br>
            <br>
            <RadioGroup v-model="commentMsgOn" size="large">
              <Radio label="所有人" ></Radio>
              &nbsp;&nbsp;
              <Radio label="我的好友" ></Radio>
              &nbsp;&nbsp;
              <Radio label="不接受任何消息提醒" ></Radio>
            </RadioGroup>
            <Divider/>
            <span>@我的消息提醒（接受谁的@消息提醒）</span>
            <br>
            <br>
            <RadioGroup v-model="atMsgOn" size="large">
              <Radio label="所有人" ></Radio>
              &nbsp;&nbsp;
              <Radio label="我的好友" ></Radio>
              &nbsp;&nbsp;
              <Radio label="不接受任何消息提醒" ></Radio>
            </RadioGroup>
            <Divider/>
            <span>收到的赞消息提醒</span>
            <br>
            <br>
            <RadioGroup v-model="thumpMsgOn" size="large">
              <Radio label="开启" ></Radio>
              &nbsp;&nbsp;
              <Radio label="关闭" ></Radio>
            </RadioGroup>
          </div>
        </Card>
      </div>
      <div class="col-md-5 column">
        <Card style="width:360px">
          <div style="text-align: left">
            <span>任何形式的互动（评论，留言，转载）</span>
            <br>
            <br>
            <RadioGroup v-model="behaviorOn" size="large">
              <Radio label="开启" ></Radio>
              &nbsp;&nbsp;
              <Radio label="关闭" ></Radio>
            </RadioGroup>
          </div>
        </Card>
        <br>
        <Card style="width:360px">
          <div style="text-align: left">
            <span>谁可以评论我的文章</span>
            <br>
            <br>
            <RadioGroup v-model="commentOn" size="large">
              <Radio label="所有人" ></Radio>
              <Radio label="我的好友" ></Radio>
              <Radio label="不接受任何评论" ></Radio>
            </RadioGroup>
          </div>
        </Card>
        <br>
        <Card style="width:360px">
          <div style="text-align: left">
            <span>谁可以给我私信留言</span>
            <br>
            <br>
            <RadioGroup v-model="leaveOn" size="large">
              <Radio label="所有人" ></Radio>
              <Radio label="我的好友" ></Radio>
              <Radio label="不接受任何留言" ></Radio>
            </RadioGroup>
          </div>
        </Card>
        <br>
        <Card style="width:360px">
          <div style="text-align: left">
            <span>谁可以转载我的文章</span>
            <br>
            <br>
            <RadioGroup v-model="forwardOn" size="large">
              <Radio label="所有人" ></Radio>
              <Radio label="我的好友" ></Radio>
              <Radio label="不接受任何转载" ></Radio>
            </RadioGroup>
          </div>
        </Card>
      </div>
    </div>
</template>
<script>
  export default {
      components: {},

      data(){
          return{
              msgOn: '开启',
              commentMsgOn: '所有人',
              atMsgOn: '所有人',
              thumpMsgOn: '开启',
              leaveMsgOn: '所有人',
              behaviorOn: '开启',
              commentOn: '所有人',
              leaveOn: '所有人',
              forwardOn: '所有人',
          }
      },
      methods:{

      }
  }
</script>
