package train.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import train.client.core.handlers.RecipeBookHandler;
import train.common.core.managers.TierRecipe;
import train.common.core.managers.TierRecipeManager;
import train.common.inventory.TrainCraftingManager;
import train.common.items.ItemRecipeBook;
import train.common.items.ItemRollingStock;
import train.common.library.BlockIDs;
import train.common.library.Info;
import train.common.library.ItemIDs;
import train.common.recipes.ShapedTrainRecipes;
import train.common.recipes.ShapelessTrainRecipe;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiRecipeBook extends GuiScreen {
	/** The player editing the book */
	private final EntityPlayer editingPlayer;
	private final ItemStack itemstackBook;

	/** Update ticks since the gui was opened */
	private int bookImageWidth = 206;
	private int bookImageHeight = 200;
	public static int bookTotalPages = 102;
	private int currPage;
	private int currRecipe;
	public ArrayList<String> leftPage = new ArrayList<String>();
	public ArrayList<String> leftPageImage = new ArrayList<String>();
	public ArrayList<ArrayList> leftPageItemStacks = new ArrayList<ArrayList>();
	public ArrayList<String> rightPage = new ArrayList<String>();
	public ArrayList<String> rightPageImage = new ArrayList<String>();
	public ArrayList<ArrayList> rightPageItemStacks = new ArrayList<ArrayList>();
	private List recipeListWB = RecipeBookHandler.workbenchListCleaner(TrainCraftingManager.getInstance().getRecipeList());
	private List<TierRecipe> recipeList = RecipeBookHandler.assemblyListCleaner(TierRecipeManager.getInstance().getRecipeList());

	private GuiButtonNextPage buttonRead;
	private GuiButtonNextPage buttonNextPage;
	private GuiButtonNextPage buttonPreviousPage;
	private GuiButtonNextPage buttonBack;
	private RenderItem renderItem = new RenderItem();

	public GuiRecipeBook(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack) {
		this.editingPlayer = par1EntityPlayer;
		this.itemstackBook = par2ItemStack;
		this.currPage = this.itemstackBook.getTagCompound().getInteger("currPage");
		this.currRecipe = this.itemstackBook.getTagCompound().getInteger("currRecipe");

		addPage("", "", "left", null);
		addPage("", "", "right", null);
		addPage("欢迎使用TC指导书! \n这本书涵盖了所有关于TC的知识.\n本MOD作者:\nSpitfire4466,\nMrbrutal\nBukkit接口作者: \nDV8FromTheWorld\n致谢CovertJaguar的帮助与其提供的API.\n", "", "left", new ArrayList<StackToDraw>() {
			{
				add(new StackToDraw(new ItemStack(ItemIDs.minecartLocoBR80_DB.item), 20, 16));
				add(new StackToDraw(new ItemStack(BlockIDs.trainWorkbench.block), 170, 16));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartCabooseWork.item), 60, 175));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartTankWagon_DB.item), 80, 175));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartFlatCartRail_DB.item), 100, 175));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartFreightWagon_DB.item), 120, 175));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartPassengerBlue.item), 140, 175));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartV60_DB.item), 160, 175));
			}
		});
		addPage("模型制作人:\nDAYdiecast,\nhelldiver,\nBlockStormTwo,\nChandlerBingUA.\nGitHub 1.7.10合作团队: \nEternal BlueFlame,\nNitroxydeX,\nFirEmerald,\nHagurd\n \n官网地址(已失效):\nhttp://traincraft-mod.\nblogspot.com", "", "right", new ArrayList<StackToDraw>() {
			{
				add(new StackToDraw(new ItemStack(ItemIDs.minecartLocoSteamAdler.item), 20, 16));
				add(new StackToDraw(new ItemStack(BlockIDs.assemblyTableII.block), 170, 16));

				add(new StackToDraw(new ItemStack(ItemIDs.minecartLocoForneyRed.item), 20, 175));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartPassengerBlue.item), 40, 175));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartPassengerBlue.item), 60, 175));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartFreightWagon_DB.item), 80, 175));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartWood.item), 100, 175));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartCabooseLogging.item), 120, 175));
			}
		});
		addPage("世界生成:\n\n 默认情况下,世界会自然生成油矿与石油金沙.如果您不想生成这两种矿物,可以在.miecraft/config/Traincraft.cfg文件中将ENABLE_ORE_SPAWN从true改成false.\n注意,这只对未探索过的区块生效,而已经存在的矿石可以直接冶炼为材料", "", "left", new ArrayList<StackToDraw>() {
			{ 
				add(new StackToDraw(new ItemStack(BlockIDs.oreTC.block, 1, 1), 60, 160));
				add(new StackToDraw(new ItemStack(BlockIDs.oreTC.block, 1, 2), 120, 160));
			}
		});

		addPage("键位:\n想要与火车交互(例如进入火车,显示库存空间)右键火车在地面上投射的阴影上方(也就是实体).\n当您进入火车后,按下R(默认是R键)打开GUI面板: 在这里您可以加注燃料,水(尽蒸汽机车有效),额外的存储空间,设置刹车等等.\n",
				"", "right", new ArrayList<StackToDraw>() {
			{
				add(new StackToDraw(new ItemStack(Items.coal), 60, 160));
				add(new StackToDraw(new ItemStack(Items.water_bucket), 120, 160));
			}
		});

		addPage("分别按下W或者S键可以向前或者向后运行火车(注意,移动的方向是相对的,在第一人称下,视角面向机车正前方时W为前进,S为后退,如果视角面向机车后方则S为前进,W为后退).\n按H(默认键位是H)使用汽笛.\n在工作车厢中按R打开工作台(默认键位R),按F打开熔炉(默认键位F).\n", "", "left", new ArrayList<StackToDraw>() {
			{
				add(new StackToDraw(new ItemStack(ItemIDs.minecartLocoBR80_DB.item), 20, 16));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartGP7Red.item), 170, 16));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartLocoCherepanov.item), 40, 145));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartWork.item), 60, 145));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartCabooseWork.item), 80, 145));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartLocoForneyRed.item), 100, 145));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartShunter.item), 120, 145));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartLocoBR01_DB.item), 140, 145));
			}
		});
		addPage("齐柏林飞艇:\n右键齐柏林飞艇进入飞艇. \n进入飞艇后按R(默认键位R)打开GUI面板. \n按WASD键移动飞艇. \n按下Y(默认键位Y)上升高度,按下X(默认键位X)下降高度,需要保持高度时按下C(默认键位C). \n", "", "right",
				new ArrayList<StackToDraw>() {
			{
				add(new StackToDraw(new ItemStack(ItemIDs.airship.item), 20, 16));
				add(new StackToDraw(new ItemStack(ItemIDs.balloon.item), 170, 16));
				add(new StackToDraw(new ItemStack(ItemIDs.propeller.item), 40, 135));
				add(new StackToDraw(new ItemStack(ItemIDs.steamengine.item), 60, 135));
				add(new StackToDraw(new ItemStack(Items.boat), 120, 135));
				add(new StackToDraw(new ItemStack(Items.stick), 140, 135));

			}
		});
		addPage("燃料:\n蒸汽机车需要煤炭和水.\n煤水车只能装载煤炭与水.煤水车与蒸汽机车连接后,煤水车会自动向蒸汽机车供应煤炭与水.\n煤水车只能装载煤炭与水.煤水车与蒸汽机车连接后,煤水车会自动向蒸汽机车供应煤炭与水.\n", "", "left", new ArrayList<StackToDraw>() {
			{
				add(new StackToDraw(new ItemStack(ItemIDs.minecartTender.item), 20, 16));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartLocomogulBlue.item), 170, 16));
				add(new StackToDraw(new ItemStack(Items.coal), 40, 155));
				add(new StackToDraw(new ItemStack(Items.water_bucket), 60, 155));
				add(new StackToDraw(new ItemStack(Blocks.planks), 80, 155));
				add(new StackToDraw(new ItemStack(Items.blaze_rod), 100, 155));
				add(new StackToDraw(new ItemStack(ItemIDs.diesel.item), 120, 155));
				add(new StackToDraw(new ItemStack(ItemIDs.refinedFuel.item), 140, 155));
			}
		});

		addPage("电力机车需要红石或者RF为基础的能量供能,也可以通过充能轨道供能.\n\n热能:\n热能大小会在HUD中热能条里显示.\n除电力机车外的机车装满燃料时不要忘记注水,注水后机车热能大小会保持在一个稳定值,一旦热能太高会导致过热,温度会迅速上升直至爆炸(建议先注水再装燃料).\n",
				"", "right", new ArrayList<StackToDraw>() {
			{
				add(new StackToDraw(new ItemStack(ItemIDs.minecartBR_E69.item), 20, 16));
				add(new StackToDraw(new ItemStack(Items.redstone), 170, 16));
			}
		});

		addPage("燃料耗尽时,热能会下降至常温.\n请等机车状态稳定后上路,注意过热会导致爆炸...\n\n路由(道岔):\n机车可以通过RC中的路由导轨进行路由.目标车厢会显示在GUI中,手拿RC中的撬棍并按住潜行再右键会重置目标车厢.无法使用列车时刻表队机车颜色进行检测,会冲突", "", "left", new ArrayList<StackToDraw>() {
			{
				add(new StackToDraw(new ItemStack(ItemIDs.firebox.item), 20, 16));
				add(new StackToDraw(new ItemStack(Blocks.tnt), 170, 16));
			}
		});
		addPage("Minecraft马力大小Mhp:\n\n每辆机车拥有不同的牵引力,这里用minecraft马力表示,单位是MHP.\n每辆机车的牵引力取决于该机车自身的马力大小(功率).\n牵引力决定了机车行驶过程中的最大速度,制动效果,加速度大小以及燃料消耗速率.\n", "", "right", new ArrayList<StackToDraw>() {
			{
				add(new StackToDraw(new ItemStack(ItemIDs.stake.item), 20, 16));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartLocoBR01_DB.item), 40, 165));
				add(new StackToDraw(new ItemStack(ItemIDs.stake.item), 60, 165));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartFreightCart2.item), 80, 165));
				add(new StackToDraw(new ItemStack(ItemIDs.stake.item), 100, 165));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartFreightClosed.item), 120, 165));
				add(new StackToDraw(new ItemStack(ItemIDs.stake.item), 140, 165));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartWatertransp.item), 160, 165));
			}
		});
		addPage("脱轨:\n\n转弯时速度不要太快,否则机车会脱轨(仅限原版轨道与RC轨道)!\n\n建议在机车入弯前降速至90KM/H以下,否则会脱轨", "", "left", new ArrayList<StackToDraw>() {
			{
				add(new StackToDraw(new ItemStack(Items.skull, 1, 1), 20, 16));
			}
		});

		addPage("牵引:\n手持连接器时,右键两辆车厢或者机车的实体部位,会在聊天框提示:已进入连接模式(Attaching mode on),这时推动两辆车厢或者机车,使其靠近,随后聊天框会提示:已连接(Attached),同时两辆车厢或机车会贴紧.\n机车是有牵引力限制的,不要使列车编组过长,若牵引机车去带动过长的车厢编组,则机车状态会受到影响.\n(影响程度会在机车GUI面板左上角显示:最高速度,自重,加速能力等)", "", "right", new ArrayList<StackToDraw>() {
			{
				add(new StackToDraw(new ItemStack(ItemIDs.stake.item), 20, 16));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartCaboose.item), 40, 165));
				add(new StackToDraw(new ItemStack(ItemIDs.stake.item), 60, 165));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartBoxCartUS.item), 80, 165));
				add(new StackToDraw(new ItemStack(ItemIDs.stake.item), 100, 165));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartFlatCartLogs_DB.item), 120, 165));
				add(new StackToDraw(new ItemStack(ItemIDs.stake.item), 140, 165));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartCD742.item), 160, 165));
			}
		});
		addPage("区块加载:\n\n每辆机车都可以加载区块.\n想要打开区块加载功能,需要使用区块加载器右键车辆实体部位.\n想要停止加载功能只需要再右键一次即可.\n", "", "left", new ArrayList<StackToDraw>() {
			{
				add(new StackToDraw(new ItemStack(ItemIDs.chunkLoaderActivator.item), 20, 16));
				add(new StackToDraw(new ItemStack(Items.ender_pearl), 170, 16));
			}
		});
		addPage("颜色:\n\n有些机车或车厢有多种纹理,可以使用染料染色.\n当您生成机车或车厢时,聊天框会提示您该机车或车厢可以使用什么颜色的燃料染色,想要涂装机车,只需要手拿原版染料右键机车实体部位即可.\n灯笼颜色是随机的,但可以通过扳手右击改成其他颜色", "", "right", new ArrayList<StackToDraw>() {
			{
				add(new StackToDraw(new ItemStack(Items.dye, 1, 1), 20, 16));
				add(new StackToDraw(new ItemStack(Items.dye, 1, 11), 170, 16));
			}
		});
		addPage("库存车(也就是运牲口的车厢):\n\n库存车职能运输生物或者实体,想要生物上车,要么吧生物蹭上去,要么使用RC的上车轨道(需要RC).\n想要生物下车,可以直接打掉车厢或使用RC的卸载轨道(需要RC)", "", "left", new ArrayList<StackToDraw>() {
			{
				add(new StackToDraw(new ItemStack(ItemIDs.minecartStockCar.item), 20, 16));
				add(new StackToDraw(new ItemStack(Items.skull, 1, 4), 170, 16));
			}
		});
		addPage("关于轨道的事I:\n铜质轨道会限制机车的巡航上限速度.\n钢制轨道会提升机车的巡航上限速度.\n速度控制轨道会限制机车的最高速度(按百分比)(使用RC撬棍右键轨道).\n红石可以给电力轨道充能并向经过的电力机车供能,收到红石信号的电力轨道会激活与它相邻两格内的所有电力轨道.\n", "", "right", new ArrayList<StackToDraw>() {
			{
				add(new StackToDraw(new ItemStack(Blocks.rail), 17, 16));
				add(new StackToDraw(new ItemStack(Blocks.rail), 17, 32));
				add(new StackToDraw(new ItemStack(Blocks.rail), 17, 48));
				add(new StackToDraw(new ItemStack(Blocks.rail), 17, 64));
				add(new StackToDraw(new ItemStack(Blocks.rail), 17, 80));
				add(new StackToDraw(new ItemStack(Blocks.rail), 17, 96));
				add(new StackToDraw(new ItemStack(Blocks.rail), 17, 112));
				add(new StackToDraw(new ItemStack(Blocks.rail), 17, 128));
				add(new StackToDraw(new ItemStack(Blocks.rail), 17, 144));
				add(new StackToDraw(new ItemStack(Blocks.rail), 17, 160));
				add(new StackToDraw(new ItemStack(Blocks.golden_rail), 170, 16));
			}
		});
		addPage("关于轨道的事II:\n用撬棍右键电力轨道就能知道电力轨道充能情况.\n连接到红石的电力轨道每2TICKS充能1个单位的能量.\n机车驻车轨道可以拦截TC机车,停下后会记录停车前的速度,在得到红石信号后恢复原有行驶速度.\n以上所有轨道必须在装有Railcraft mod的情况下才能实现.","","left",new ArrayList<StackToDraw>() {
					{
						add(new StackToDraw(new ItemStack(Blocks.activator_rail), 17, 16));
						add(new StackToDraw(new ItemStack(Blocks.golden_rail), 170, 16));
					}
				});
		addPage("钢轨部件与铜轨部件可以在RC的轧机(辊压机)内制造,轨道在原版工作台合成.\n只有TC里库存车(牲畜车)才能使用RC的装载/卸载轨道,动物装载轨道会使附近的家畜进入库存车(牲畜车),而生物装载轨道才能使所有库存车(牲畜车)附近的生物进入库存车(牲畜车).\n卸载轨道则会使库存车(牲畜车)上的所有生物离开车厢.","","right",new ArrayList<StackToDraw>() {
			{
				add(new StackToDraw(new ItemStack(Blocks.detector_rail), 17, 16));
				add(new StackToDraw(new ItemStack(Blocks.golden_rail), 170, 16));
			}
		});
		addPage("关于盾构机I:\n\n盾构机的运行与存储不同于一般的机车,想要使其启动您需要按如下方式操作:\n盾构机GUI下方的槽中放入铺路的路基(木板,沙砾,石头等).\n在燃料槽中加入碳,燃料槽下方放入轨道(无法放入本mod的轨道,仅支持原版轨道与RC轨道),在盾构机右侧的槽中放入方块(不支持所有方块)激活隧道功能(不放也能挖,建议放入,否则挖隧道时碰到水会很难处理).\n", "", "left", new ArrayList<StackToDraw>() {
			{
				add(new StackToDraw(new ItemStack(ItemIDs.minecartBuilder.item), 20, 16));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartBuilder.item), 60, 165));
				add(new StackToDraw(new ItemStack(ItemIDs.stake.item), 80, 165));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartFreightClosed.item), 100, 165));
			}
		});
		addPage("关于盾构机II:\n\n盾构机GUI上方的槽可以放置方块(不支持所有方块),防止后挖隧道时会铺在机车顶上(不放也能挖,建议放入,否则挖隧道时碰到水会很难处理).如果您给盾构机连接货车车厢,盾构机挖掘后,挖下来的方块或其他物品会自动进入货车车厢.\n\n", "", "right", new ArrayList<StackToDraw>() {
			{
				add(new StackToDraw(new ItemStack(ItemIDs.minecartBuilder.item), 20, 16));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartBuilder.item), 60, 125));
				add(new StackToDraw(new ItemStack(ItemIDs.stake.item), 80, 125));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartFreightClosed.item), 100, 125));
			}
		});
		addPage("限制:\n非常重要:\n长编组列车有限制:不要有180度的急转弯,转弯半径至少保证有三格(针对原版轨道和RC轨道),特别是非常长的机车组上会出现很奇怪的事(穿模,断连或脱轨).\n不要尝试用机车撞击连接好的列车!这样会导致碰撞箱出现重叠而引发BUG!", "", "left", new ArrayList<StackToDraw>() {
			{
				add(new StackToDraw(new ItemStack(Blocks.detector_rail), 20, 16));
				add(new StackToDraw(new ItemStack(Blocks.rail), 170, 16));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartPower.item), 40, 165));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartFlatCartWoodUS.item), 60, 165));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartBoxCartUS.item), 80, 165));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartPassenger9_2class_DB.item), 100, 165));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartFlatCartLogs_DB.item), 120, 165));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartFreightCart2.item), 140, 165));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartCaboose.item), 160, 165));
			}
		});
		addPage("制造 :\n所有的火车部件都在火车工作台上,\n火车必须在装配台上装配.\n有三种装配台:\n\nI型铁制装配台\nII型钢制装配台\nIII型高级装配台\n", "", "right", new ArrayList<StackToDraw>() {
			{
				add(new StackToDraw(new ItemStack(BlockIDs.trainWorkbench.block), 20, 16));
				add(new StackToDraw(new ItemStack(BlockIDs.assemblyTableI.block), 40, 165));
				add(new StackToDraw(new ItemStack(BlockIDs.assemblyTableII.block), 80, 165));
				add(new StackToDraw(new ItemStack(BlockIDs.assemblyTableIII.block), 140, 165));
			}
		});
		addPage("冶炼钢铁:\n钢是在平炉里冶炼得到的,平炉GUI上面的两个槽放入铁和碳,底部槽放入燃料, \n然后铁就会慢慢变成钢.\n您也可以使用其他Mod里的钢或者在其他Mod中使用本Mod的钢.\n", "", "left", new ArrayList<StackToDraw>() {
			{
				add(new StackToDraw(new ItemStack(BlockIDs.openFurnaceIdle.block), 20, 16));
				add(new StackToDraw(new ItemStack(BlockIDs.openFurnaceActive.block), 80, 155));
				add(new StackToDraw(new ItemStack(ItemIDs.graphite.item), 70, 135));
				add(new StackToDraw(new ItemStack(Items.iron_ingot), 90, 135));
				add(new StackToDraw(new ItemStack(Items.coal), 80, 175));
				add(new StackToDraw(new ItemStack(ItemIDs.steel.item), 120, 155));
			}
		});

		addPage("炼制柴油:\n柴油是在蒸馏塔中炼制自然生成的石油矿或金沙石油获得.\n在蒸馏塔GUI顶部的槽中放入石油,在底部槽中放入燃料后会开始精炼.\n在精炼后会变成液态柴油并且会根据您放入的原料数量产生随机数量的产物:塑料.\n想要装柴油,您需要用产生的塑料制成罐,然后把罐放入蒸馏塔GUI右上方的槽中.", "", "right", new ArrayList<StackToDraw>() {
			{
				add(new StackToDraw(new ItemStack(BlockIDs.distilIdle.block), 20, 16));
				add(new StackToDraw(new ItemStack(BlockIDs.distilActive.block), 150, 40));
				add(new StackToDraw(new ItemStack(BlockIDs.oreTC.block, 1, 1), 150, 20));
				add(new StackToDraw(new ItemStack(Items.coal), 150, 60));
				add(new StackToDraw(new ItemStack(ItemIDs.diesel.item), 167, 40));
				add(new StackToDraw(new ItemStack(ItemIDs.rawPlastic.item), 167, 60));
			}
		});
		addPage("关于牵引的事I: \n您可以连接两台机车.\n潜行时用连接器右键你想牵引的机车,然后给被牵引的机车加注燃料,直至温度达到正常值(这点只针对蒸汽机车).\n两台机车处于连接模式时,驾驶其中一辆机车靠近另一辆机车,最后连接(两台机车连接时需要车尾对车尾相连,否则转弯时会出现BUG).", "", "left", new ArrayList<StackToDraw>() {
			{
				add(new StackToDraw(new ItemStack(ItemIDs.minecartLocoBR80_DB.item), 20, 16));
				add(new StackToDraw(new ItemStack(ItemIDs.stake.item), 170, 16));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartCabooseWork.item), 40, 165));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartTankWagon_DB.item), 60, 165));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartFlatCartRail_DB.item), 80, 165));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartFreightWagon_DB.item), 100, 165));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartCD742.item), 120, 165));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartCD742.item), 140, 165));
			}
		});
		addPage("关于牵引的事II: \n机车有两种状态:'可以牵引(Can pull)'和'可以被牵引(Can be pulled)'.\n'可以牵引(Can pull)'表示可以牵引任何被连接的列车.\n'可以被牵引(Can be pulled)'表示可以被任何机车牵引(只能是处于可以牵引状态的机车),连接的电力机车可以共享电力.", "", "right", new ArrayList<StackToDraw>() {
			{
				add(new StackToDraw(new ItemStack(ItemIDs.minecartLoco3.item), 20, 16));
				add(new StackToDraw(new ItemStack(ItemIDs.stake.item), 170, 16));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartCD742.item), 40, 165));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartFreightWagon_DB.item), 60, 165));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartFreightWagon_DB.item), 80, 165));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartFreightWagon_DB.item), 100, 165));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartFreightWagon_DB.item), 120, 165));
				add(new StackToDraw(new ItemStack(ItemIDs.minecartCD742.item), 140, 165));
			}
		});
		addPage("护甲I: \nmod中有几件护甲.\n其中三种由皮革合成(工程师,售票员,驾驶员),与原版皮革甲无异.\n相反,合金装甲拥有特殊能力,非常的强力.\n合金装甲详情见下一页.", "", "left", new ArrayList<StackToDraw>() {
			{
				add(new StackToDraw(new ItemStack(ItemIDs.hat.item), 20, 16));
				add(new StackToDraw(new ItemStack(ItemIDs.hat_ticketMan_paintable.item), 170, 16));
				add(new StackToDraw(new ItemStack(ItemIDs.hat_driver_paintable.item), 40, 165));
				add(new StackToDraw(new ItemStack(ItemIDs.jacket_driver_paintable.item), 90, 165));
				add(new StackToDraw(new ItemStack(ItemIDs.pants_driver_paintable.item), 140, 165));
			}
		});
		addPage("护甲II: \n头盔可以提供免疫中毒效果,水下呼吸效果以及夜视效果(开火车时不会提供此效果)\n胸甲提供生命恢复,每5秒恢复半颗心.\n护腿提供防火效果.\n鞋子提供衰落保护效果.\n"+
				"售票员,驾驶员和合金装甲都可以染色(在火车工作台上)", "", "right", new ArrayList<StackToDraw>() {
			{
				add(new StackToDraw(new ItemStack(ItemIDs.helmet_suit_paintable.item), 20, 16));
				add(new StackToDraw(new ItemStack(ItemIDs.reinforcedPlates.item), 170, 16));
			}
		});
		addPage("发电机:\n本Mod有三种发电机.\n水力发电机,风力发电机以及柴油发电机,它们都是RF能量类型的发电机.水力发电机必须放在流动的水源旁边(把驱动装置也就是扇叶放向水这面就行),风力发电机的发电效果取决于风力大小.\n给柴油发电机注入燃料,使用红石启动,在正面放上RF能量管道就能发电了.\n","","left",new ArrayList<StackToDraw>() {
					{
						add(new StackToDraw(new ItemStack(BlockIDs.windMill.block, 1, 4), 20, 16));
						add(new StackToDraw(new ItemStack(BlockIDs.waterWheel.block, 1, 4), 170, 16));
					}
				});
		addPage("村民\nTC为每个村庄添加了一种新的村民和一个火车站.\n这种新增加的村民会和您交易所有TC的物品.\n","","right",new ArrayList<StackToDraw>() {
					{
						add(new StackToDraw(new ItemStack(ItemIDs.jacket_driver_paintable.item, 1, 4), 20, 16));
						add(new StackToDraw(new ItemStack(ItemIDs.hat_driver_paintable.item, 1, 4), 170, 16));
					}
				});
		addPage("已知BUG:\n-不支持急转弯(180度)\n-当您的列车反向慢速通过一个道岔时,您的列车可能会由于判定时间而被道岔分开(不会断连)\n-重启游戏或重进存档后,您连接好的列车可能会因为碰撞箱的缘故而重叠,断开或者各种视觉BUG\n-不要连续使用4个及以上的TC斜坡轨道,容易脱轨!\n-TC斜坡轨道会在y=160以上位置失效 ",
				"", "left", new ArrayList<StackToDraw>() {
					{
						add(new StackToDraw(new ItemStack(Items.skull, 1, 4), 20, 16));
						add(new StackToDraw(new ItemStack(Items.skull, 1, 4), 170, 16));
					}
				});
		
		addPage("建议:\n由于碰撞箱的问题,列车之间必须链接或牵引,不要尝试用您的机车去碰撞连接好的列车组\nTC和RC同时存在时,将优先使用TC的牵引系统,停用RC的牵引系统. \n","","right",new ArrayList<StackToDraw>() {
					{
						add(new StackToDraw(new ItemStack(ItemIDs.minecartTankWagon_DB.item), 20, 16));
						add(new StackToDraw(new ItemStack(BlockIDs.oreTC.block), 170, 16));
						add(new StackToDraw(new ItemStack(ItemIDs.minecartCaboose3.item), 40, 155));
						add(new StackToDraw(new ItemStack(ItemIDs.minecartFreightWellcar.item), 60, 155));
						add(new StackToDraw(new ItemStack(ItemIDs.minecartOpenWagon.item), 80, 155));
						add(new StackToDraw(new ItemStack(ItemIDs.minecartStockCar.item), 100, 155));
						add(new StackToDraw(new ItemStack(ItemIDs.minecartOpenWagon.item), 120, 155));
						add(new StackToDraw(new ItemStack(ItemIDs.minecartBR_E69.item), 140, 155));
					}
				});
		
		addPage("在本书后面的页面里,您可以找到所有火车的合成配方以及火车的装配方法.\n强烈建议您自己猜测装配方法...\n我们希望您能喜欢和享受这个Mod!\n\nSpitfire4466 and MrBrutal", "", "left", new ArrayList<StackToDraw>() {
			{
				add(new StackToDraw(new ItemStack(BlockIDs.trainWorkbench.block), 20, 16));
				add(new StackToDraw(new ItemStack(ItemIDs.hat.item), 40, 155));
				add(new StackToDraw(new ItemStack(ItemIDs.jacket.item), 90, 155));
				add(new StackToDraw(new ItemStack(ItemIDs.overalls.item), 140, 155));
			}
		});
		addPage("以下是官方在这里的原话:\nthis page was intentionally left blank, as a joke.\n这里感谢\n@流星落岩(我的世界火车mod吧)\n参考他的蓝本教程翻译得出这个版本,这次发布的将是稳定版,希望各位玩的开心,玩得愉快\n\n译者:格罗夫8(我的世界火车mod吧)/winmox(bilibili)","","right",null);
		if (rightPage != null && recipeList != null && recipeListWB != null)
			bookTotalPages = this.rightPage.size() + (recipeList.size() / 2) + (recipeListWB.size() / 2);
	}

	public class StackToDraw {
		private ItemStack stack;
		private int x;
		private int y;

		public StackToDraw(ItemStack stack, int x, int y) {
			this.stack = stack;
			this.x = x;
			this.y = y;
		}

		public ItemStack getItemStack() {
			return stack;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}
	}

	private void addPage(String text, String image, String side, ArrayList<StackToDraw> stacks) {
		if (side.equals("left")) {
			leftPage.add(text);
			leftPageImage.add(image);
			leftPageItemStacks.add(stacks);
		}
		if (side.equals("right")) {
			rightPage.add(text);
			rightPageImage.add(image);
			rightPageItemStacks.add(stacks);
		}
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		this.buttonList.clear();

		int var1 = (this.width) / 2;
		int var2 = (this.height) / 2;
		this.buttonList.add(this.buttonBack = new GuiButtonNextPage(4, var1 + 150, var2 + 80, 23, 13, true));
		this.buttonList.add(this.buttonRead = new GuiButtonNextPage(3, var1 - 8, var2 + 98, 40, 20, true));
		this.buttonList.add(this.buttonNextPage = new GuiButtonNextPage(1, var1 + 150, var2 + 80, 23, 13, true));
		this.buttonList.add(this.buttonPreviousPage = new GuiButtonNextPage(2, var1 - 180, var2 + 80, 23, 13, false));
		this.updateButtons();
	}

	private void updateButtons() {
		this.buttonBack.visible = (this.currPage == bookTotalPages-1);
		this.buttonBack.showButton = true;
		this.buttonRead.visible = (this.currPage == 0);
		this.buttonRead.showButton = false;
		this.buttonNextPage.visible = (this.currPage > 0 && this.currPage < bookTotalPages - 1);
		this.buttonNextPage.showButton = (this.currPage > 0 && this.currPage < bookTotalPages - 1);
		this.buttonPreviousPage.visible = this.currPage > 0;
		this.buttonPreviousPage.showButton = this.currPage > 0;
	}

	/**
	 * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
	 */
	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.enabled) {
			if (par1GuiButton.id == 1) {
				if (this.currPage < bookTotalPages - 1) {
					++this.currPage;
					this.currRecipe += 2;
				}
			}
			else if (par1GuiButton.id == 2) {
				if (this.currPage > 0) {
					--this.currPage;
					this.currRecipe -= 2;
				}
			}
			else if (par1GuiButton.id == 3) {
				if (this.currPage == 0) {
					++this.currPage;
					this.currRecipe += 2;
				}
			}
			else if (par1GuiButton.id == 4) {
				if (this.currPage == bookTotalPages-1) {
					this.currPage = 0;
					this.currRecipe = 0;
				}
			}
			this.updateButtons();
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		String pageIndic;
		int var9;
		int var5 = (this.width) / 2;
		int var6 = (this.height) / 2 - bookImageHeight / 2;

		if (this.currPage > 0) {
			//GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			mc.renderEngine.bindTexture(new ResourceLocation(Info.resourceLocation,Info.bookPrefix + "bookright.png"));
			this.drawTexturedModalRect(var5, var6, 0, 0, this.bookImageWidth, this.bookImageHeight + 20);
			//GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			mc.renderEngine.bindTexture(new ResourceLocation(Info.resourceLocation,Info.bookPrefix + "bookleft.png"));
			var5 -= this.bookImageWidth;
			this.drawTexturedModalRect(var5, var6, 256 - this.bookImageWidth, 0, this.bookImageWidth, this.bookImageHeight);
		}
		else {
			mc.renderEngine.bindTexture(new ResourceLocation(Info.resourceLocation,Info.bookPrefix + "bookcover.png"));
			this.drawTexturedModalRect(var5 - 55, var6 - 15, 0, 0, 256, 256);
		}

		pageIndic = String.format(StatCollector.translateToLocal("book.pageIndicator"), new Object[] {this.currPage + 1, this.bookTotalPages});

		var9 = this.fontRendererObj.getStringWidth(pageIndic);
		if (this.currPage > 0) {
			this.fontRendererObj.drawString(pageIndic, var5 - var9 + this.bookImageWidth - 44, var6 + 7, 0);
		}
		super.drawScreen(par1, par2, par3);

		if (this.currPage < rightPage.size()) {
			this.fontRendererObj.drawSplitString(leftPage.get(this.currPage), var5 + 36, var6 + 16 + 16, 140, 0);

			this.fontRendererObj.drawSplitString(rightPage.get(this.currPage), var5 + 250, var6 + 16 + 16, 140, 0);

			GL11.glEnable(32826);
			RenderHelper.enableGUIStandardItemLighting();
			if (this.leftPageItemStacks != null && this.leftPageItemStacks.get(this.currPage) != null && this.leftPageItemStacks.get(this.currPage).get(0) != null) {
				for (int t = 0; t < this.leftPageItemStacks.get(this.currPage).size(); t++) {
					if (this.leftPageItemStacks.get(this.currPage).get(t) != null) {
						renderItem.renderItemIntoGUI(this.fontRendererObj, this.mc.renderEngine, ((StackToDraw) this.leftPageItemStacks.get(this.currPage).get(t)).getItemStack(), var5 + ((StackToDraw) this.leftPageItemStacks.get(this.currPage).get(t)).getX(), var6 + ((StackToDraw) this.leftPageItemStacks.get(this.currPage).get(t)).getY());
					}
				}
			}
			if (this.rightPageItemStacks != null && this.rightPageItemStacks.get(this.currPage) != null && this.rightPageItemStacks.get(this.currPage).get(0) != null) {
				for (int t = 0; t < this.rightPageItemStacks.get(this.currPage).size(); t++) {
					if (this.rightPageItemStacks.get(this.currPage).get(t) != null) {
						renderItem.renderItemIntoGUI(this.fontRendererObj, this.mc.renderEngine, ((StackToDraw) this.rightPageItemStacks.get(this.currPage).get(t)).getItemStack(), var5 + ((StackToDraw) this.rightPageItemStacks.get(this.currPage).get(t)).getX() + 210, var6 + ((StackToDraw) this.rightPageItemStacks.get(this.currPage).get(t)).getY());
					}
				}
			}
			GL11.glDisable(32826);
		}
		if (this.currPage > rightPage.size() - 1) {
			//System.out.println((rightPage.size()*2) -1);
			int page = this.currRecipe - (rightPage.size() * 2) + 1;
			if (!(page > recipeListWB.size() - 1)) {
				drawWorkBenchBackground(recipeListWB, var5, var6, 0, var9, "right");
				drawWorkBenchBackground(recipeListWB, var5, var6, 0, var9, "left");
				RenderHelper.enableGUIStandardItemLighting();
				drawWorkBenchRecipe(recipeListWB, var5, var6, page - 1, var9, "right");
				drawWorkBenchRecipe(recipeListWB, var5, var6, page, var9, "left");
			}
			else if ((page - recipeListWB.size()) >= 0 && (page - recipeListWB.size()) < recipeList.size() && recipeList.get(page - recipeListWB.size()) != null) {
				drawAssemblyBackground(recipeList, var5 - 125, var6 - 33, page - recipeListWB.size(), var9, "right");
				drawAssemblyBackground(recipeList, var5 - 50, var6 - 33, page - recipeListWB.size() - 1, var9, "left");
				RenderHelper.enableGUIStandardItemLighting();
				drawAssemblyRecipe(recipeList, var5 - 125, var6 - 33, page - recipeListWB.size(), var9, "right");
				drawAssemblyRecipe(recipeList, var5 - 50, var6 - 33, page - recipeListWB.size() - 1, var9, "left");
			}
		}
		GL11.glDisable(GL11.GL_LIGHTING);
	}

	private void drawAssemblyBackground(List<TierRecipe> recipeList, int var5, int var6, int page, int var9, String side) {
		if (page < 0)
			return;
		int tier = recipeList.get(page).getTier();
		if (tier == 1)
			mc.renderEngine.bindTexture(new ResourceLocation(Info.resourceLocation,Info.TEX_TIER_I));
		if (tier == 2)
			mc.renderEngine.bindTexture(new ResourceLocation(Info.resourceLocation,Info.TEX_TIER_II));
		if (tier == 3)
			mc.renderEngine.bindTexture(new ResourceLocation(Info.resourceLocation,Info.TEX_TIER_III));
		//if (side.equals("right"))
		//GL11.glScaled(0.7, 0.7, 0.7);
		if (side.equals("left"))
			this.drawTexturedModalRect(var5 + 70, var6 + 50, 0, 0, 177, 163);
		if (side.equals("right"))
			this.drawTexturedModalRect(var5 + 340, var6 + 50, 0, 0, 177, 163);
	}

	private void drawWorkBenchBackground(List<ShapedTrainRecipes> recipeListWB, int var5, int var6, int page, int var9, String side) {
		//int var4 = this.mc.renderEngine.getTexture("/gui/crafting.png");
		//GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(new ResourceLocation(Info.resourceLocation,Info.guiPrefix+"crafting_table.png"));
		if (side.equals("left"))
			this.drawTexturedModalRect(var5 + 20, var6 + 50, 0, 0, 177, 80);
		if (side.equals("right"))
			this.drawTexturedModalRect(var5 + 215, var6 + 50, 0, 0, 177, 80);
	}

	private void drawWorkBenchRecipe(List recipeList, int var5, int var6, int page, int var9, String side) {
		if (recipeList.get(page) == null)
			return;
		ItemStack[] itemList = new ItemStack[9];
		ItemStack itemOutput = null;
		if (recipeList.get(page) instanceof ShapedTrainRecipes) {
			itemList = ((ShapedTrainRecipes) recipeList.get(page)).recipeItems;
			itemOutput = ((ShapedTrainRecipes) recipeList.get(page)).getRecipeOutput();
		}
		if (recipeList.get(page) instanceof ShapelessTrainRecipe) {
			List<ItemStack> itemListShapeless = ((ShapelessTrainRecipe) recipeList.get(page)).recipeItems;
			for (int t = 0; t < itemListShapeless.size(); t++) {
				if (itemListShapeless != null && itemListShapeless.get(t) != null)
					itemList[t] = itemListShapeless.get(t);
			}
			itemOutput = ((ShapelessTrainRecipe) recipeList.get(page)).getRecipeOutput();
		}

		//System.out.println(itemOutput);
		int offset = 0;
		if (side.equals("right"))
			offset = 194;
		GL11.glEnable(32826);
		if (itemList[0] != null)
			renderItem.renderItemIntoGUI(this.fontRendererObj, this.mc.renderEngine, itemList[0], var5 + 50 + offset, var6 + 67);
		if (itemList[1] != null)
			renderItem.renderItemIntoGUI(this.fontRendererObj, this.mc.renderEngine, itemList[1], var5 + 68 + offset, var6 + 67);
		if (itemList[2] != null)
			renderItem.renderItemIntoGUI(this.fontRendererObj, this.mc.renderEngine, itemList[2], var5 + 86 + offset, var6 + 67);
		if (itemList[3] != null)
			renderItem.renderItemIntoGUI(this.fontRendererObj, this.mc.renderEngine, itemList[3], var5 + 50 + offset, var6 + 85);
		if (itemList[4] != null)
			renderItem.renderItemIntoGUI(this.fontRendererObj, this.mc.renderEngine, itemList[4], var5 + 68 + offset, var6 + 85);
		if (itemList[5] != null)
			renderItem.renderItemIntoGUI(this.fontRendererObj, this.mc.renderEngine, itemList[5], var5 + 86 + offset, var6 + 85);
		if (itemList[6] != null)
			renderItem.renderItemIntoGUI(this.fontRendererObj, this.mc.renderEngine, itemList[6], var5 + 50 + offset, var6 + 103);
		if (itemList[7] != null)
			renderItem.renderItemIntoGUI(this.fontRendererObj, this.mc.renderEngine, itemList[7], var5 + 68 + offset, var6 + 103);
		if (itemList[8] != null)
			renderItem.renderItemIntoGUI(this.fontRendererObj, this.mc.renderEngine, itemList[8], var5 + 86 + offset, var6 + 103);
		if (itemOutput != null && itemOutput.getItem() !=null)
			renderItem.renderItemIntoGUI(this.fontRendererObj, this.mc.renderEngine, itemOutput, var5 + 145 + offset, var6 + 85);
		if (itemOutput != null && itemOutput.getItem() !=null)
			this.fontRendererObj.drawString(itemOutput.getItem().getItemStackDisplayName(itemOutput), var5 + 20 + offset, var6 + 40, 0);
		if (itemOutput != null)
			this.fontRendererObj.drawString("制造途径: 火车工作台", var5 + 20 + offset, var6 + 130, 0);
		if (itemOutput != null) {
			for (int z = 0; z < RecipeBookHandler.vanillaWorkTableRecipes.length; z++) {
				if (itemOutput.getItem()!= null && RecipeBookHandler.vanillaWorkTableRecipes[z]!=null && RecipeBookHandler.vanillaWorkTableRecipes[z].equals(itemOutput.getItem().getItemStackDisplayName(itemOutput))) {
					this.fontRendererObj.drawString("也能被制造于: 原版工作台", var5 + 20 + offset, var6 + 140, 0);
					break;
				}
			}
		}
		GL11.glDisable(32826);
	}

	private void drawAssemblyRecipe(List<TierRecipe> recipeList, int var5, int var6, int page, int var9, String side) {
		if (page < 0)
			return;
		int tier = recipeList.get(page).getTier();

		List<ItemStack> itemList = recipeList.get(page).getInput();
		int offset = 0;
		if (side.equals("right"))
			offset = 271;
		GL11.glEnable(32826);
		if (itemList.get(0) != null)
			renderItem.renderItemIntoGUI(this.fontRendererObj, this.mc.renderEngine, itemList.get(0), var5 + 94 + offset, var6 + 76);
		if (itemList.get(0) != null)
			renderItem.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.renderEngine, itemList.get(0), var5 + 94 + offset, var6 + 76);
		if (itemList.get(1) != null)
			renderItem.renderItemIntoGUI(this.fontRendererObj, this.mc.renderEngine, itemList.get(1), var5 + 113 + offset, var6 + 143);
		if (itemList.get(1) != null)
			renderItem.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.renderEngine, itemList.get(1), var5 + 113 + offset, var6 + 143);
		if (itemList.get(2) != null)
			renderItem.renderItemIntoGUI(this.fontRendererObj, this.mc.renderEngine, itemList.get(2), var5 + 148 + offset, var6 + 143);
		if (itemList.get(2) != null)
			renderItem.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.renderEngine, itemList.get(2), var5 + 148 + offset, var6 + 143);
		if (itemList.get(3) != null)
			renderItem.renderItemIntoGUI(this.fontRendererObj, this.mc.renderEngine, itemList.get(3), var5 + 214 + offset, var6 + 143);
		if (itemList.get(3) != null)
			renderItem.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.renderEngine, itemList.get(3), var5 + 214 + offset, var6 + 143);
		if (itemList.get(4) != null)
			renderItem.renderItemIntoGUI(this.fontRendererObj, this.mc.renderEngine, itemList.get(4), var5 + 148 + offset, var6 + 77);
		if (itemList.get(4) != null)
			renderItem.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.renderEngine, itemList.get(4), var5 + 148 + offset, var6 + 77);
		if (itemList.get(5) != null)
			renderItem.renderItemIntoGUI(this.fontRendererObj, this.mc.renderEngine, itemList.get(5), var5 + 184 + offset, var6 + 77);
		if (itemList.get(5) != null)
			renderItem.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.renderEngine, itemList.get(5), var5 + 184 + offset, var6 + 77);
		if (itemList.get(6) != null)
			renderItem.renderItemIntoGUI(this.fontRendererObj, this.mc.renderEngine, itemList.get(6), var5 + 149 + offset, var6 + 110);
		if (itemList.get(6) != null)
			renderItem.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.renderEngine, itemList.get(6), var5 + 149 + offset, var6 + 110);
		if (itemList.get(7) != null)
			renderItem.renderItemIntoGUI(this.fontRendererObj, this.mc.renderEngine, itemList.get(7), var5 + 185 + offset, var6 + 110);
		if (itemList.get(7) != null)
			renderItem.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.renderEngine, itemList.get(7), var5 + 185 + offset, var6 + 110);
		if (itemList.get(8) != null)
			renderItem.renderItemIntoGUI(this.fontRendererObj, this.mc.renderEngine, itemList.get(8), var5 + 94 + offset, var6 + 110);
		if (itemList.get(8) != null)
			renderItem.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.renderEngine, itemList.get(8), var5 + 94 + offset, var6 + 110);
		if (itemList.get(9) != null)
			renderItem.renderItemIntoGUI(this.fontRendererObj, this.mc.renderEngine, itemList.get(9), var5 + 214 + offset, var6 + 77);
		if (itemList.get(9) != null)
			renderItem.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.renderEngine, itemList.get(9), var5 + 214 + offset, var6 + 77);
		ItemStack output = recipeList.get(page).getOutput();
		if (output != null && side.equals("left"))
			renderItem.renderItemIntoGUI(this.fontRendererObj, this.mc.renderEngine, output, var5 + 162, var6 + 177);
		if (output != null && side.equals("right"))
			renderItem.renderItemIntoGUI(this.fontRendererObj, this.mc.renderEngine, output, var5 + 432, var6 + 177);
		String name = "";
		if (output != null && output.getItem() instanceof ItemRollingStock)
			name = output.getDisplayName();
		if (side.equals("left")) {
			this.fontRendererObj.drawString("Tier: " + tier, var5 - var9 + this.bookImageWidth - 56, var6 + 40, 0);
			this.fontRendererObj.drawString(name, var5 - var9 + this.bookImageWidth - 55, var6 + 56, 0xffffff);
		}
		if (side.equals("right")) {
			this.fontRendererObj.drawString(name, var5 - var9 + this.bookImageWidth + 215, var6 + 56, 0xffffff);
			this.fontRendererObj.drawString("Tier: " + tier, var5 - var9 + this.bookImageWidth + 338, var6 + 40, 0);
		}			
		GL11.glDisable(32826);
	}

	@Override
	public void onGuiClosed() {
		ItemRecipeBook.page = this.currPage;
		ItemRecipeBook.recipe = this.currRecipe;
		this.itemstackBook.getTagCompound().setInteger("currPage", this.currPage);
		this.itemstackBook.getTagCompound().setInteger("currRecipe", this.currRecipe);
		super.onGuiClosed();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	protected void keyTyped(char par1, int par2) {
		if(par2 == 1 || par2 == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
			this.mc.thePlayer.closeScreen();
		}
	}
}