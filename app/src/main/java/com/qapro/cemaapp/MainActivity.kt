package com.qapro.cemaapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.system.exitProcess

// 評価項目のデータ構造
@Serializable
data class CemaQuestion(
    val id: Int,
    val title: String,
    val options: List<CemaOption>
)

@Serializable
data class CemaOption(
    val score: Int,
    val label: String
)

@Serializable
data class CemaData(
    val questions: List<CemaQuestion>,
    val weights: List<List<Double>>,
    val biases: List<Double>
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                CemaAppNavigation()
            }
        }
    }
}

fun loadCemaData(context: Context): CemaData? {
    return try {
        val jsonString = context.assets.open("cema_data.json").bufferedReader().use { it.readText() }
        Json.decodeFromString<CemaData>(jsonString)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Composable
fun CemaAppNavigation() {
    val context = LocalContext.current
    // 画面遷移を管理する状態 (0: 規約画面, 1: 評価画面, 2: 結果画面)
    var currentScreen by remember { mutableStateOf(0) }

    // ユーザーの回答を保持するマップ（項目ID -> 選択されたスコア。未選択はnull）
    val answers = remember { mutableStateMapOf<Int, Int?>() }

    // JSONデータを初期化
    val cemaData = remember { loadCemaData(context) }

    if (cemaData == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("データの読み込みに失敗しました")
        }
        return
    }

    when (currentScreen) {
        0 -> DisclaimerScreen(
            onAgree = { currentScreen = 1 },
            onDisagree = { exitProcess(0) }
        )
        1 -> EvaluationScreen(
            questions = cemaData.questions,
            answers = answers,
            onNavigateToResult = { currentScreen = 2 }
        )
        2 -> ResultScreen(
            cemaData = cemaData,
            answers = answers,
            onReset = {
                answers.clear()
                currentScreen = 1
            }
        )
    }
}

// --- 【画面1】免責同意・防御画面 ---
@Composable
fun DisclaimerScreen(onAgree: () -> Unit, onDisagree: () -> Unit) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "企業倫理成熟度評価 (CEMA) v2\n- 試用版プロトタイプ -",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color(0xFF1A1A1A),
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.White)
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            Text(
                text = "【重要】本アプリのご利用に関する規約と免責事項",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = """
                    本アプリは、組織の「企業倫理成熟度」を自己診断・評価するための【研究開発中の試用版（プロトタイプ）】です。今後のアップデートにより、評価基準や仕様は予告なく変更される場合があります。試用期間中はすべての機能を無償でご利用いただけます。

                    以下の利用規約および免責事項をよくお読みいただき、同意の上でご利用ください。

                    1. 目的の限定
                    本アプリは、ユーザーが【自組織（自社）の現状を客観的に把握し、内部での品質改善活動および経営層への働きかけを行うこと】を唯一の目的として提供されています。特定の他社を誹謗中傷する目的での使用を固く禁じます。

                    2. データおよび評価結果の責任
                    選択肢の判定基準は、過去の一般的なITインシデント事例を参考に学術的・統計的に作成された一般的なモデルです。特定の企業を直接指定・評価するものではありません。アプリによって算出されたスコアおよび改善提案は、【ユーザー自身の入力に基づく自己申告の診断結果】であり、開発者はその正確性、正当性、および結果から生じるいかなる損害（社会的信用への影響を含む）についても一切の責任を負いません。

                    3. フィードバックの受付
                    本アプリの評価ロジックの改善提案やバグ報告などがございましたら、以下のグループメールアドレスまでご連絡ください。
                    
                    お問い合わせ先：(※グループメールアドレスを想定)
                """.trimIndent(),
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = Color(0xFF333333)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = onDisagree,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9E9E9E)),
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) {
                Text("同意しない", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = onAgree,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            ) {
                Text("同意して開始", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// --- 【画面2】10尺度の評価入力画面 ---
@Composable
fun EvaluationScreen(
    questions: List<CemaQuestion>,
    answers: MutableMap<Int, Int?>,
    onNavigateToResult: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "企業倫理成熟度評価 (CEMA) v2",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = "すべての項目は過去5年間の最悪・最低なインシデントを元に選択してください（未入力は自動的に -1点 ）。\n※選択肢には社会的に公知となった某社の重大インシデント例が含まれています。自社においてこれらに類する事故や事象があった場合は、該当する項目にチェックを入れてください。",
            fontSize = 12.sp,
            color = Color(0xFFD32F2F), // 重要な案内として少し目立たせる赤みのある色
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 16.dp)
        )


        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            questions.forEach { question ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = question.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1A1A)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // ラジオボタンの展開
                        question.options.forEach { option ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = (answers[question.id] == option.score),
                                        onClick = { answers[question.id] = option.score }
                                    )
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (answers[question.id] == option.score),
                                    onClick = { answers[question.id] = option.score }
                                )
                                Text(
                                    text = option.label,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onNavigateToResult,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))
        ) {
            Text("組織の倫理成熟度を判定する", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// --- 【画面3】診断結果・経営層への提言画面 ---
@Composable
fun ResultScreen(
    cemaData: CemaData,
    answers: Map<Int, Int?>,
    onReset: () -> Unit
) {
    val scrollState = rememberScrollState()

    // --- 加重スコア計算ロジック (Matrix Calculation) ---
    // Xベクトル: 各質問のスコア (未回答は -1)
    val x = cemaData.questions.map { answers[it.id]?.toDouble() ?: -1.0 }
    
    // Y = WX + B
    val y = List(10) { i ->
        var sum = 0.0
        for (j in 0 until 10) {
            sum += cemaData.weights[i][j] * x[j]
        }
        sum + cemaData.biases[i]
    }

    // 総スコア S = sum(Y)
    val totalScoreDouble = y.sum()
    val totalScore = totalScoreDouble.toInt()
    val unansweredCount = answers.values.count { it == null }

    // 点数に応じたメッセージ判定
    val resultTitle: String
    val resultMessage: String
    val resultColor: Color

    if (totalScore >= 20) {
        resultTitle = "【優良】倫理成熟企業モデル"
        resultMessage = "品質文化が経営トップから現場まで極めて高いレベルで浸透しています。今後も現場の品質部長の出荷停止権限などの強い独立性を維持・サポートし、この健全なガバナンス体制を継続してください。"
        resultColor = Color(0xFF2E7D32) // 緑
    } else if (totalScore >= 0) {
        resultTitle = "【注意】部分的不健全組織"
        resultMessage = "現時点では致命的な崩壊には至っていませんが、一部の項目において投資不足や隠蔽体質、丸投げの兆候が見られます。今すぐトップ層がコミットメントを見直し、現場の悲鳴に耳を傾けて投資を再開しなければ、近い将来に重大な社会的事故に発展するリスクがあります。"
        resultColor = Color(0xFFEF6C00) // オレンジ
    } else {
        resultTitle = "【警告】倫理・技術の完全崩壊組織"
        resultMessage = "組織の倫理観および技術力が、経営層から現場に至るまで完全に麻痺・崩壊しています。不祥事の隠蔽、現場への責任転嫁、形骸化した再発防止策はすでに社会に見透かされています。「企業は社会の公器」という原点に立ち返り、経営陣の刷新、ならびに品質部門の権限と技術力を根本から再構築しない限り、企業の存続自体が不可能です。"
        resultColor = Color(0xFFC62828) // 赤
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "診断結果レポート",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .background(Color(0xFFFAFAFA))
                .padding(16.dp)
        ) {
            // スコア表示
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "総合倫理成熟度スコア (加重計算後)", fontSize = 14.sp, color = Color.Gray)
                    Text(
                        text = "$totalScore 点",
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold,
                        color = resultColor
                    )
                    if (unansweredCount > 0) {
                        Text(text = "(未入力項目 $unansweredCount 件によるペナルティ分を含む)", fontSize = 11.sp, color = Color.Red)
                    }
                    Text(text = "※重み行列とバイアスベクトルによる多角的な算出結果です。", fontSize = 11.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            // 組織判定
            Text(
                text = resultTitle,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = resultColor,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // 経営層への提言文
            Text(
                text = resultMessage,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = Color(0xFF222222)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "※本試用版へのフィードバックやロジック改善の提案がございましたら、開発グループメールアドレスまでお寄せください。",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onReset,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF757575))
        ) {
            Text("トップに戻って再評価する", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
