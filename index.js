import Anthropic from "@anthropic-ai/sdk";
require('dotenv').config();

const client = new Anthropic({
  // Gọi biến môi trường từ file .env lên thay vì viết thẳng mã ra đây
  apiKey: process.env.ANTHROPIC_API_KEY,
});

async function testClaude() {
  const response = await client.messages.create({
    model: "claude-3-5-sonnet-20241022",
    max_tokens: 200,
    messages: [
      { role: "user", content: "Giải thích thuật toán quicksort" }
    ],
  });

  console.log(response.content[0].text);
}

testClaude();